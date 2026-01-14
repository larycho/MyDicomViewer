package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.events.DicomDirLoadedEvent;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.models.DicomDirectory;
import org.example.mydicomviewer.models.DicomDirectoryRecord;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.services.DicomDirLoadManager;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.services.FolderLoadedEventService;
import org.example.mydicomviewer.views.filelist.FileListPanel;
import org.example.mydicomviewer.views.filelist.FileNodeType;
import org.example.mydicomviewer.views.filelist.FileTreeNode;
import org.example.mydicomviewer.workers.OpenFragmentedFileWorker;

import javax.swing.tree.DefaultMutableTreeNode;
import java.util.List;

@Singleton
public class FileListUpdater implements FileLoadedListener, DicomDirLoadedListener, FolderLoadedListener {

    private final FileListPanel fileListPanel;

    @Inject
    public FileListUpdater(
            FileListPanel fileListPanel,
            FileLoadEventService fileLoadEventService,
            DicomDirLoadManager dicomDirLoadManager,
            FolderLoadedEventService folderLoadedEventService
    ) {
        this.fileListPanel = fileListPanel;
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

        if (fileListPanel.getState().containsFile(file.getFile())) {
            return;
        }

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
        return record.getType().equals(FileNodeType.PRIVATE);
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

    @Override
    public void folderLoaded(FolderLoadedEvent event) {
        FileTreeNode tree = event.getTree();
        createRootNode(tree);
    }

    private void createRootNode(FileTreeNode node) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(node);
        addChildrenOfRoot(node);
    }

    private void addChildrenOfRoot(FileTreeNode root) {
        List<FileTreeNode> children = root.getChildren();

        for (FileTreeNode child : children) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(child);
            fileListPanel.addFileToList(node);
            addChildrenOfNode(child, node);
        }
    }

    private void addChildrenOfNode(FileTreeNode parentNode, DefaultMutableTreeNode parent) {
        List<FileTreeNode> children = parentNode.getChildren();

        // Browse through the children
        for (FileTreeNode child : children) {
            // Add child node to the panel
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(child);
            fileListPanel.addFileToList(node, parent);
            // Recursively add its children to the panel
            addChildrenOfNode(child, node);
        }
    }




}
