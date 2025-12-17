package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.events.DicomDirLoadedEvent;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.models.DicomDirectory;
import org.example.mydicomviewer.models.DicomDirectoryRecord;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.processing.dicomdir.DicomDirPath;
import org.example.mydicomviewer.services.DicomDirLoadManager;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.services.FolderLoadedEventService;
import org.example.mydicomviewer.views.filelist.FileListPanel;
import org.example.mydicomviewer.views.filelist.MyTreeNode;
import org.example.mydicomviewer.workers.OpenFragmentedFileWorker;

import javax.swing.tree.DefaultMutableTreeNode;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

//    public FileListUpdater(FileListPanel fileListPanel) {
//        this.fileListPanel = fileListPanel;
//    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        if (event.getSource() instanceof OpenFragmentedFileWorker) {
            return;
        }
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

    @Override
    public void folderLoaded(FolderLoadedEvent event) {
        MyTreeNode tree = event.getTree();
        createRootNode(tree);
    }

    private void createRootNode(MyTreeNode node) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(node);
        addChildrenOfRoot(node);
    }

    private void addChildrenOfRoot(MyTreeNode root) {
        List<MyTreeNode> children = root.getChildren();

        for (MyTreeNode child : children) {
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(child);
            fileListPanel.addFileToList(node);
            addChildrenOfNode(child, node);
        }
    }

    private void addChildrenOfNode(MyTreeNode parentNode, DefaultMutableTreeNode parent) {
        List<MyTreeNode> children = parentNode.getChildren();

        // Browse through the children
        for (MyTreeNode child : children) {
            // Add child node to the panel
            DefaultMutableTreeNode node = new DefaultMutableTreeNode(child);
            fileListPanel.addFileToList(node, parent);
            // Recursively add its children to the panel
            addChildrenOfNode(child, node);
        }
    }




}
