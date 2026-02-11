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

    @Inject
    public FolderLoadManagerImpl(FolderLoadedEventService folderLoadedEventService,
                                 FileTreeNodeService fileTreeNodeService) {
        this.folderLoadedEventService = folderLoadedEventService;
        this.fileTreeNodeService = fileTreeNodeService;
    }

    @Override
    public void openFolder(File folder) {
        ArrayList<File> files = getFileList(folder);

        List<FileNodeData> data = fileTreeNodeService.getFileNodeDataList(files);

        fireFolderLoadedEvent(data);
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

    private void fireFolderLoadedEvent(List<FileNodeData> data) {
        FolderLoadedEvent event = new FolderLoadedEvent(data, this);
        folderLoadedEventService.notify(event);
    }

}
