package org.example.mydicomviewer.services;

import com.google.inject.Inject;
import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.views.filelist.FileNodeData;
import org.example.mydicomviewer.views.filelist.FileTreeNodeService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class FolderLoadManagerImpl implements FolderLoadManager {

    private final FolderLoadedEventService folderLoadedEventService;
    private final FileTreeNodeService fileTreeNodeService;
    private final OpenFileManager openFileManager;

    @Inject
    public FolderLoadManagerImpl(FolderLoadedEventService folderLoadedEventService,
                                 FileTreeNodeService fileTreeNodeService,
                                 OpenFileManager openFileManager) {
        this.folderLoadedEventService = folderLoadedEventService;
        this.fileTreeNodeService = fileTreeNodeService;
        this.openFileManager = openFileManager;
    }

    @Override
    public void openFolder(File folder) {
        ArrayList<File> files = getFileList(folder);

        List<FileNodeData> data = fileTreeNodeService.getFileNodeDataList(files);

        fireFolderLoadedEvent(data);

        triggerFileOpen(data);
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
                    .forEach(p -> files.add(p.toFile()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return files;
    }

    private void fireFolderLoadedEvent(List<FileNodeData> data) {
        FolderLoadedEvent event = new FolderLoadedEvent(data, this);
        folderLoadedEventService.notify(event);
    }

    private void triggerFileOpen(List<FileNodeData> data) {
        if (data.isEmpty()) {
            return;
        }
        FileNodeData firstFile = data.get(0);
        if (firstFile.getFile().isEmpty()) {
            return;
        }

        List<File> allFiles = findFilesFromTheSameSeries(firstFile, data);

        if (allFiles.isEmpty()) {
            openFileManager.openFileUsingWorker(firstFile.getFile().get());
        }
        else {
            openFileManager.openFragmentedFileUsingWorker(allFiles);
        }
    }

    private List<File> findFilesFromTheSameSeries(FileNodeData mainFile, List<FileNodeData> data) {
        List<FileNodeData> nodes = fileTreeNodeService.getFileNodesWithSeriesUid(mainFile, data);
        return fileTreeNodeService.convertFileNodesToList(nodes);
    }

}
