package org.mydicomviewer.services.implementations;

import com.google.inject.Inject;
import org.mydicomviewer.events.FolderLoadedEvent;
import org.mydicomviewer.events.services.FolderLoadedEventService;
import org.mydicomviewer.services.FolderLoadManager;
import org.mydicomviewer.services.OpenFileManager;
import org.mydicomviewer.ui.filelist.FileNodeData;
import org.mydicomviewer.ui.filelist.FileTreeNodeService;

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
        long startTime = System.nanoTime();
        ArrayList<File> files = getFileList(folder);

        List<FileNodeData> data = fileTreeNodeService.getFileNodeDataList(files);

        fireFolderLoadedEvent(data);

        triggerFileOpen(data);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1_000_000;
        System.out.println((endTime - startTime) / 10000);
        System.out.println(duration + " ms");
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
