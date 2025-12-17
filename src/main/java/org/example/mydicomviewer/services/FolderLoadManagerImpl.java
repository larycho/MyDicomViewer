package org.example.mydicomviewer.services;

import com.google.inject.Inject;
import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.processing.dicomdir.DicomDirPath;
import org.example.mydicomviewer.processing.dicomdir.DicomDirPathProcessor;
import org.example.mydicomviewer.views.filelist.MyTreeNode;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class FolderLoadManagerImpl implements FolderLoadManager {

    private DicomDirPathProcessor dicomDirPathProcessor;
    private FolderLoadedEventService folderLoadedEventService;

    @Inject
    public FolderLoadManagerImpl(DicomDirPathProcessor dicomDirPathProcessor,
                                 FolderLoadedEventService folderLoadedEventService) {
        this.dicomDirPathProcessor = dicomDirPathProcessor;
        this.folderLoadedEventService = folderLoadedEventService;
    }

    @Override
    public void openFolder(File folder) {
        ArrayList<File> files = getFileList(folder);

        Map<DicomDirPath, ArrayList<File>> mappings = groupFiles(files);

        MyTreeNode tree = mapToTree(mappings);

        fireFolderLoadedEvent(tree);
    }

    // Files are grouped according to PatientID, StudyID and Series Number
    private Map<DicomDirPath, ArrayList<File>> groupFiles(ArrayList<File> files) {
        Map<DicomDirPath, ArrayList<File>> groupedFiles = new HashMap<>();

        for (File file : files) {
            DicomDirPath path = dicomDirPathProcessor.getDicomDirPath(file);
            groupedFiles.putIfAbsent(path, new ArrayList<>());
            groupedFiles.get(path).add(file);
        }

        return groupedFiles;
    }

    private ArrayList<File> getFileList(File folder) {
        ArrayList<File> files = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(folder.toPath())) {
            walk.filter(p -> !Files.isDirectory(p))
                .filter(p -> p.toFile().getName().endsWith(".dcm"))
                .forEach(p -> {files.add(p.toFile());});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    private MyTreeNode mapToTree(Map<DicomDirPath, ArrayList<File>> mappings) {
        MyTreeNode tree = new MyTreeNode();
        tree.setType("root");

        for (DicomDirPath key : mappings.keySet()) {
            String patientId = key.getPatientId();

            boolean found = false;
            for (MyTreeNode child : tree.getChildren()) {
                if ( (child.getType().equals("PATIENT")) && (Objects.equals(child.getText(), key.getPatientId())) ) {
                    browseStudy(child, key, mappings);
                    found = true;
                    break;
                }
            }
            if (!found) {
                MyTreeNode patientNode = new MyTreeNode();
                patientNode.setType("PATIENT");
                patientNode.setText(patientId);
                tree.addChild(patientNode);
                browseStudy(patientNode, key, mappings);
            }

        }
        return tree;
    }

    private void browseStudy(MyTreeNode node, DicomDirPath key, Map<DicomDirPath, ArrayList<File>> mappings) {
        String studyId = key.getStudyId();

        boolean found = false;
        for (MyTreeNode child : node.getChildren()) {
            if ( (child.getType().equals("STUDY")) && (child.getText().equals(studyId)) ) {
                browseSeries(child, key, mappings);
                found = true;
                break;
            }
        }
        if (!found) {
            MyTreeNode studyNode = new MyTreeNode();
            studyNode.setType("STUDY");
            studyNode.setText(studyId);
            node.addChild(studyNode);
            browseSeries(studyNode, key, mappings);
        }
    }

    private void browseSeries(MyTreeNode node, DicomDirPath key, Map<DicomDirPath, ArrayList<File>> mappings) {
        String seriesId = key.getSeriesId();

        boolean found = false;
        for (MyTreeNode child : node.getChildren()) {
            if ( (child.getType().equals("SERIES")) && (Objects.equals(child.getText(), seriesId)) ) {
                browseImages(child, key, mappings);
                setMainFile(child);
                found = true;
                break;
            }
        }
        if (!found) {
            MyTreeNode seriesNode = new MyTreeNode();
            seriesNode.setType("SERIES");
            seriesNode.setText(seriesId);
            node.addChild(seriesNode);
            browseImages(seriesNode, key, mappings);
            setMainFile(seriesNode);
        }
        setMainFile(node);
    }

    private static void setMainFile(MyTreeNode node) {
        if (node.getChildren().size() > 1) {
            List<MyTreeNode> children = node.getChildren();
            List<File> mainFile = new ArrayList<>();
            for (MyTreeNode child : children) {
                child.setPartialFile(true);
                mainFile.add(child.getFile());
            }
            for (MyTreeNode child : children) {
                child.setMainFile(mainFile);
            }
        }
    }

    private void browseImages(MyTreeNode node, DicomDirPath key, Map<DicomDirPath, ArrayList<File>> mappings) {
        String instanceId = key.getInstanceId();
        ArrayList<File> files = mappings.get(key);

        for (File file : files) {
            MyTreeNode imageNode = new MyTreeNode();
            imageNode.setType("IMAGE");
            imageNode.setText(file.getName());
            imageNode.setLeaf(true);
            imageNode.setFile(file);

            if (files.size() > 1) {
                imageNode.setPartialFile(true);
                imageNode.setMainFile(files);
            }
            node.addChild(imageNode);
        }
    }

    private void fireFolderLoadedEvent(MyTreeNode treeNode) {
        FolderLoadedEvent event = new FolderLoadedEvent(treeNode, this);
        folderLoadedEventService.notify(event);
    }

}
