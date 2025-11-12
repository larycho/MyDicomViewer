package org.example.mydicomviewer.views.filelist;

import javax.swing.*;
import javax.swing.tree.*;

public class FileListScrollPane extends JScrollPane {

    JTree tree;
    DefaultTreeModel treeModel;
    DefaultMutableTreeNode root;

    public FileListScrollPane() {
        super();
        root = new DefaultMutableTreeNode("Root");
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setRootVisible(false);

        setViewportView(tree);
    }

    public void addNode(DefaultMutableTreeNode node) {
        treeModel.insertNodeInto(node, root, root.getChildCount());

        TreeNode[] nodes = treeModel.getPathToRoot(node);
        TreePath path = new TreePath(nodes);
        tree.scrollPathToVisible(path);
    }
}
