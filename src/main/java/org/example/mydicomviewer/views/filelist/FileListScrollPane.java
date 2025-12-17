package org.example.mydicomviewer.views.filelist;

import org.example.mydicomviewer.models.DicomDirectoryRecord;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.services.OpenFileManager;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class FileListScrollPane extends JScrollPane {

    JTree tree;
    DefaultTreeModel treeModel;
    DefaultMutableTreeNode root;
    OpenFileManager openFileManager;

    public FileListScrollPane(OpenFileManager openFileManager) {
        super();
        this.openFileManager = openFileManager;
        root = new DefaultMutableTreeNode("Root");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setRootVisible(false);
        tree.setCellRenderer(new TreeIconRenderer());
        addActionListeners();
        setViewportView(tree);
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
        Object userObject = node.getUserObject();
        if (userObject instanceof DicomDirectoryRecord) {
            DicomDirectoryRecord record = (DicomDirectoryRecord) userObject;
            String filename = record.getText();
            File directory = record.getMainDicomDirectory().getDirectory().getParentFile();
            Optional<Path> path = findFile(directory, filename);
            if (path.isPresent()) {
                File file = path.get().toFile();
                openFileManager.openFileUsingWorker(file);
            }
        }
        else if (userObject instanceof MyTreeNode imageNode) {
            if (imageNode.isLeaf() && imageNode.getType().equals("IMAGE")) {
                if (imageNode.isPartialFile()) {
                    List<File> files = imageNode.getMainFile();
                    openFileManager.openFragmentedFileUsingWorker(files);
                }
                else {
                    openFileManager.openFileUsingWorker(imageNode.getFile());
                }
            }
        }
        else if (userObject instanceof DicomFile) {
//            DicomFile dicomFile = (DicomFile) userObject;
//            File file = dicomFile.getFile();
//            openFileManager.openFileUsingWorker(file);
        }
    }

    private Optional<Path> findFile(File directory, String fileName) {
        try (Stream<Path> stream = Files.walk(directory.toPath())) {
            Optional<Path> result = stream.filter(path -> path.getFileName().toString().equals(fileName)).findFirst();
            return result;
        }
        catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void addNode(DefaultMutableTreeNode node) {
        treeModel.insertNodeInto(node, root, root.getChildCount());

        TreeNode[] nodes = treeModel.getPathToRoot(node);
        TreePath path = new TreePath(nodes);
        tree.scrollPathToVisible(path);
    }

    public void addNode(DefaultMutableTreeNode node, DefaultMutableTreeNode parent) {
        treeModel.insertNodeInto(node, parent, parent.getChildCount());

        TreeNode[] nodes = treeModel.getPathToRoot(node);
        TreePath path = new TreePath(nodes);
        tree.scrollPathToVisible(path);
    }
}
