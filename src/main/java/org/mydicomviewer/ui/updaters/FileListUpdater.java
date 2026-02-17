package org.mydicomviewer.ui.updaters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.events.DicomDirLoadedEvent;
import org.mydicomviewer.events.FileLoadedEvent;
import org.mydicomviewer.events.FolderLoadedEvent;
import org.mydicomviewer.events.listeners.DicomDirLoadedListener;
import org.mydicomviewer.events.listeners.FileLoadedListener;
import org.mydicomviewer.events.listeners.FolderLoadedListener;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.services.DicomDirLoadManager;
import org.mydicomviewer.events.services.FileLoadEventService;
import org.mydicomviewer.events.services.FolderLoadedEventService;
import org.mydicomviewer.ui.filelist.FileListPanel;
import org.mydicomviewer.ui.filelist.FileNodeData;
import org.mydicomviewer.ui.filelist.FileTreeNodeService;
import org.mydicomviewer.workers.OpenFragmentedFileWorker;
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
        fileListPanel.setExpanded();
        List<FileNodeData> foundFiles = dicomDirLoadedEvent.getExtractedFiles();

        for (FileNodeData file : foundFiles) {
            fileListPanel.addFileToList(file);
        }
    }

    @Override
    public void folderLoaded(FolderLoadedEvent event) {

        fileListPanel.clear();
        fileListPanel.setExpanded();
        List<FileNodeData> files = event.getData();

        for (FileNodeData file : files) {
            fileListPanel.addFileToList(file);
        }

    }

}
