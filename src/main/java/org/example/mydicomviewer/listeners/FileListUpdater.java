package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.events.DicomDirLoadedEvent;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.models.DicomDirectory;
import org.example.mydicomviewer.models.DicomDirectoryRecord;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.services.DicomDirLoadManager;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.views.filelist.FileListPanel;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

@Singleton
public class FileListUpdater implements FileLoadedListener, DicomDirLoadedListener {

    private FileListPanel fileListPanel;
    private FileLoadEventService fileLoadEventService;

    @Inject
    public FileListUpdater(
            FileListPanel fileListPanel,
            FileLoadEventService fileLoadEventService,
            DicomDirLoadManager dicomDirLoadManager
    ) {
        this.fileListPanel = fileListPanel;
        this.fileLoadEventService = fileLoadEventService;
        fileLoadEventService.addListener(this);
        dicomDirLoadManager.addListener(this);
    }

    public FileListUpdater(FileListPanel fileListPanel) {
        this.fileListPanel = fileListPanel;
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        DicomFile file = event.getFile();
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(file);
        fileListPanel.addFileToList(node);
    }

    @Override
    public void dicomDirLoaded(DicomDirLoadedEvent dicomDirLoadedEvent) {
        DicomDirectory directory = dicomDirLoadedEvent.getDicomDirectory();
        addRootNode(directory);
    }

    private void addRootNode(DicomDirectory directory) {
        DicomDirectoryRecord rootRecord = directory.getRoot();
        addToRootNode(rootRecord);
    }

    private void addToRootNode(DicomDirectoryRecord record) {
        List<DicomDirectoryRecord> children = record.getChildren();

        // Browse through the children
        for (DicomDirectoryRecord child : children) {
            if (isRecordPrivate(child)) {
                continue;
            }
            // Add child node to the root node of the panel
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(child);
            fileListPanel.addFileToList(node);
            // Recursively add its children to the panel
            addChildren(child, node);
        }
    }

    private boolean isRecordPrivate(DicomDirectoryRecord record) {
        return record.getType().contains("PRIVATE");
    }

    private void addChildren(DicomDirectoryRecord record, DefaultMutableTreeNode parent) {
        List<DicomDirectoryRecord> children = record.getChildren();

        // Browse through the children
        for (DicomDirectoryRecord child : children) {
            if (isRecordPrivate(child)) {
                continue;
            }
            // Add child node to the panel
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(child);
            fileListPanel.addFileToList(node, parent);
            // Recursively add its children to the panel
            addChildren(child, node);
        }
    }
}
