package org.example.mydicomviewer.services;

import com.google.inject.Inject;
import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.processing.dicomdir.DicomDirPath;
import org.example.mydicomviewer.processing.dicomdir.DicomDirPathProcessor;
import org.example.mydicomviewer.views.filelist.FileNodeType;
import org.example.mydicomviewer.views.filelist.FileTreeNode;

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

        FileTreeNode tree = mapToTree(mappings);

        fireFolderLoadedEvent(tree);
    }

    // Files are grouped according to PatientID, StudyID and Series Number
    private Map<DicomDirPath, ArrayList<File>> groupFiles(ArrayList<File> files) {
        Map<DicomDirPath, ArrayList<File>> groupedFiles = new HashMap<>();

        for (File file : files) {
            Optional<DicomDirPath> result = dicomDirPathProcessor.getDicomDirPath(file);
            if (result.isPresent()) {
                DicomDirPath path = result.get();
                groupedFiles.putIfAbsent(path, new ArrayList<>());
                groupedFiles.get(path).add(file);
            }
        }

        return groupedFiles;
    }

    private ArrayList<File> getFileList(File folder) {
        ArrayList<File> files = new ArrayList<>();

        try (Stream<Path> walk = Files.walk(folder.toPath())) {
            walk.filter(p -> !Files.isDirectory(p))
                    .filter(p -> !p.toFile().isHidden())
                    .filter(p -> {
                        String fileName = p.toFile().getName();
                        return fileName.endsWith(".dcm") || !fileName.contains(".");
                    })
                    .forEach(p -> {files.add(p.toFile());});
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    private FileTreeNode mapToTree(Map<DicomDirPath, ArrayList<File>> mappings) {
        FileTreeNode tree = new FileTreeNode();
        tree.setRoot(true);

        for (DicomDirPath key : mappings.keySet()) {
            String patientId = key.getPatientId() == null ? "" : key.getPatientId();

            boolean found = false;
            for (FileTreeNode child : tree.getChildren()) {
                if ( (child.getNodeType().equals(FileNodeType.PATIENT)) && (Objects.equals(child.getId(), patientId)) ) {
                    browseStudy(child, key, mappings);
                    found = true;
                    break;
                }
            }
            if (!found) {
                FileTreeNode patientNode = new FileTreeNode();
                patientNode.setNodeType(FileNodeType.PATIENT);
                patientNode.setId(patientId == null ? "" : patientId);
                patientNode.setText(patientId == null ? "" : patientId);
                tree.addChild(patientNode);
                browseStudy(patientNode, key, mappings);
            }

        }
        //tree.sortChildren();
        return tree;
    }

    private void browseStudy(FileTreeNode node, DicomDirPath key, Map<DicomDirPath, ArrayList<File>> mappings) {
        String studyId = key.getStudyIdOrUid();

        boolean found = false;
        for (FileTreeNode child : node.getChildren()) {
            if ((child.getNodeType().equals(FileNodeType.STUDY)) && (child.getId().equals(key.getStudyIdOrUid() == null ? "" : key.getStudyIdOrUid()))) {
                browseSeries(child, key, mappings);
                found = true;
                break;
            }
        }
        if (!found) {
            FileTreeNode studyNode = new FileTreeNode();
            studyNode.setNodeType(FileNodeType.STUDY);
            studyNode.setId(studyId == null ? "" : studyId);
            studyNode.setText(key.getStudyId() == null ? "" : key.getStudyId());
            node.addChild(studyNode);
            browseSeries(studyNode, key, mappings);
        }
    }

    private void browseSeries(FileTreeNode node, DicomDirPath key, Map<DicomDirPath, ArrayList<File>> mappings) {
        String seriesId = key.getSeriesIdOrUid();

        boolean found = false;
        for (FileTreeNode child : node.getChildren()) {
            if ( (child.getNodeType().equals(FileNodeType.SERIES)) && (Objects.equals(child.getId(), seriesId)) ) {
                browseImages(child, key, mappings);
                setMainFile(child);
                found = true;
                break;
            }
        }
        if (!found) {
            FileTreeNode seriesNode = new FileTreeNode();
            seriesNode.setNodeType(FileNodeType.SERIES);
            seriesNode.setId(seriesId == null ? "" : seriesId);
            seriesNode.setText(key.getSeriesId() == null ? "" : key.getSeriesId());
            node.addChild(seriesNode);
            browseImages(seriesNode, key, mappings);
            setMainFile(seriesNode);
        }
        setMainFile(node);
    }

    private static void setMainFile(FileTreeNode node) {
        if (node.getChildren().size() > 1) {
            List<FileTreeNode> children = node.getChildren();
            List<File> mainFile = new ArrayList<>();
            for (FileTreeNode child : children) {
                child.setPartialFile(true);
                mainFile.add(child.getFile());
            }
            for (FileTreeNode child : children) {
                child.setMainFile(mainFile);
            }
        }
    }

    private void browseImages(FileTreeNode node, DicomDirPath key, Map<DicomDirPath, ArrayList<File>> mappings) {
        String instanceId = key.getInstanceId();
        ArrayList<File> files = mappings.get(key);

        for (File file : files) {
            FileTreeNode imageNode = new FileTreeNode();
            imageNode.setNodeType(FileNodeType.IMAGE);
            imageNode.setId(instanceId == null ? "" : instanceId);
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

    private void fireFolderLoadedEvent(FileTreeNode treeNode) {
        FolderLoadedEvent event = new FolderLoadedEvent(treeNode, this);
        folderLoadedEventService.notify(event);
    }

}
