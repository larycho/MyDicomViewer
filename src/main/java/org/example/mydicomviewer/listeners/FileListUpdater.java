package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.views.filelist.FileListPanel;

import javax.swing.tree.DefaultMutableTreeNode;

@Singleton
public class FileListUpdater implements FileLoadedListener {

    private FileListPanel fileListPanel;
    private FileLoadEventService fileLoadEventService;

    @Inject
    public FileListUpdater(FileListPanel fileListPanel, FileLoadEventService fileLoadEventService) {
        this.fileListPanel = fileListPanel;
        this.fileLoadEventService = fileLoadEventService;
        fileLoadEventService.addListener(this);
    }

    public FileListUpdater(FileListPanel fileListPanel) {
        this.fileListPanel = fileListPanel;
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        DicomFile file = event.getFile();
        String filename = file.getFileName();

        DefaultMutableTreeNode node = new DefaultMutableTreeNode(filename);
        fileListPanel.addFileToList(node);
    }
}
