package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.views.filelist.FileListPanel;

import javax.swing.tree.DefaultMutableTreeNode;

public class FileListUpdater implements FileLoadedListener {

    private FileListPanel fileListPanel;

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
