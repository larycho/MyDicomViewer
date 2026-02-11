package org.example.mydicomviewer.views.filelist;
import org.example.mydicomviewer.services.FragmentedFileEventService;
import org.example.mydicomviewer.services.OpenFileManager;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class FileListScrollPane extends JScrollPane {

    FileTree fileTree;
    JTree tree;

    OpenFileManager openFileManager;
    FragmentedFileEventService fragmentedFileEventService;

    public FileListScrollPane(OpenFileManager openFileManager,
                              FragmentedFileEventService fragmentedFileEventService) {
        super();
        this.openFileManager = openFileManager;
        this.fragmentedFileEventService = fragmentedFileEventService;

        fileTree = new FileTree();
        tree = new JTree(fileTree.getTreeModel());
        tree.setRootVisible(false);
        tree.setCellRenderer(new TreeIconRenderer());
        addActionListeners();
        setViewportView(tree);
    }

    public void clear() {
        fileTree.cleanList();
    }

    public void addActionListeners() {
        tree.addMouseListener(new MouseAdapter() {
           @Override
           public void mouseClicked(MouseEvent e) {
               if (e.getClickCount() == 2) {
                   handleDoubleClick(e);
               }
           }
        });
    }

    public void handleDoubleClick(MouseEvent e) {
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path != null) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

            if (node.isLeaf()) {
                openFileFromNode(node);
            }
        }
    }

    private void openFileFromNode(DefaultMutableTreeNode node) {

        if (!(node.getUserObject() instanceof FileNode fileNode)) {
            return;
        }

        if (fileNode.getType() != FileNodeType.IMAGE) {
            return;
        }

        if (fileNode.getData() == null) {
            return;
        }

        if (fileNode.getData().getSeriesInstanceUid().isEmpty()) {
            handleFileWithMissingSeriesUid(fileNode);
        }
        else {
            handleFileWithPresentSeriesUid(fileNode);
        }

    }

    private void handleFileWithMissingSeriesUid(FileNode fileNode) {

        if (fileNode.getData().getFile().isPresent()) {
            openFileManager.openFileUsingWorker(fileNode.getData().getFile().get());
        }
    }

    private void handleFileWithPresentSeriesUid(FileNode fileNode) {

        if (fileNode.getData().getSeriesInstanceUid().isEmpty()) {
            return;
        }

        String seriesUid = fileNode.getData().getSeriesInstanceUid().get();

        List<File> files = fileTree.getFilesWithSeriesUid(seriesUid);
        Integer index = fileNode.getData().getInstanceNumber().orElse(null);

        if (files.size() > 1 && index != null) {
            fragmentedFileEventService.notifyListeners(files, index - 1);
        }
        else if (files.size() == 1) {
            openFileManager.openFileUsingWorker(files.get(0));
        }
    }

    public void addFile(FileNodeData data) {
        if (fileTree.duplicateIsPresent(data)) {
            return;
        }
        DefaultMutableTreeNode node = fileTree.addFile(data);
        TreePath path = fileTree.getPathToRoot(node);
        tree.scrollPathToVisible(path);
    }
}
