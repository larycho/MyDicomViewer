package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.events.DicomDirLoadedEvent;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.services.DicomDirLoadManager;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.services.FolderLoadedEventService;
import org.example.mydicomviewer.views.filelist.*;
import org.example.mydicomviewer.workers.OpenFragmentedFileWorker;
import java.util.List;

@Singleton
public class FileListUpdater implements FileLoadedListener, DicomDirLoadedListener, FolderLoadedListener {

    private final FileListPanel fileListPanel;
    private final FileTreeNodeService fileTreeNodeService;

    @Inject
    public FileListUpdater(
            FileListPanel fileListPanel,
            FileLoadEventService fileLoadEventService,
            DicomDirLoadManager dicomDirLoadManager,
            FolderLoadedEventService folderLoadedEventService,
            FileTreeNodeService fileTreeNodeService
    ) {
        this.fileListPanel = fileListPanel;
        this.fileTreeNodeService = fileTreeNodeService;
        fileLoadEventService.addListener(this);
        dicomDirLoadManager.addListener(this);
        folderLoadedEventService.addListener(this);
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        if (event.getSource() instanceof OpenFragmentedFileWorker) {
            return;
        }
        DicomFile file = event.getFile();

        FileNodeData nodeData = fileTreeNodeService.getFileNodeData(file);
        fileListPanel.addFileToList(nodeData);
    }

    @Override
    public void dicomDirLoaded(DicomDirLoadedEvent dicomDirLoadedEvent) {
        fileListPanel.clear();
        List<FileNodeData> foundFiles = dicomDirLoadedEvent.getExtractedFiles();

        for (FileNodeData file : foundFiles) {
            fileListPanel.addFileToList(file);
        }
    }

    @Override
    public void folderLoaded(FolderLoadedEvent event) {

        fileListPanel.clear();
        List<FileNodeData> files = event.getData();

        for (FileNodeData file : files) {
            fileListPanel.addFileToList(file);
        }
    }

}
