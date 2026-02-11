package org.example.mydicomviewer.views.filelist;

import javax.swing.tree.*;
import java.io.File;
import java.util.*;

public class FileTree {

    private final DefaultMutableTreeNode root;
    private final DefaultTreeModel treeModel;

    public FileTree() {
        root = new DefaultMutableTreeNode();
        treeModel = new DefaultTreeModel(root);
    }

    public DefaultTreeModel getTreeModel() {
        return treeModel;
    }

    public void cleanList() {

        root.removeAllChildren();
        treeModel.reload(root);
    }

    public List<File> getFilesWithSeriesUid(String seriesUid) {

        // Using set at first to prevent duplicates
        Set<File> files = new LinkedHashSet<>();

        Enumeration<TreeNode> e = root.depthFirstEnumeration();

        while (e.hasMoreElements()) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();

            if (!(node.getUserObject() instanceof FileNode fileNode)) {
                continue;
            }
            FileNodeData data = fileNode.getData();

            // Make sure series UID is not missing
            if (data.getSeriesInstanceUid().isEmpty()) {
                continue;
            }
            String currentSeriesUid = data.getSeriesInstanceUid().get();

            if (currentSeriesUid.equals(seriesUid)) {
                data.getFile().ifPresent(files::add);
            }

        }

        return new ArrayList<>(files);
    }

    public boolean duplicateIsPresent(FileNodeData fileNodeData) {

        Enumeration<TreeNode> e = root.breadthFirstEnumeration();

        while (e.hasMoreElements()) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();

            if (node.getUserObject() instanceof FileNode fileNode) {

                FileNodeData data = fileNode.getData();

                if (data.getSeriesInstanceUid().equals(fileNodeData.getSeriesInstanceUid()) &&
                    data.getFile().equals(fileNodeData.getFile()) &&
                    data.getSopInstanceUid().equals(fileNodeData.getSopInstanceUid())) {
                        return true;
                }
            }
        }

        return false;
    }

    public DefaultMutableTreeNode addFile(FileNodeData data) {

        DefaultMutableTreeNode patientNode = findOrCreateNode(FileNodeType.PATIENT, data, root);
        DefaultMutableTreeNode studyNode = findOrCreateNode(FileNodeType.STUDY, data, patientNode);
        DefaultMutableTreeNode seriesNode = findOrCreateNode(FileNodeType.SERIES, data, studyNode);

        return findOrCreateNode(FileNodeType.IMAGE, data, seriesNode);
    }

    public TreePath getPathToRoot(DefaultMutableTreeNode node) {
        TreeNode[] nodes = treeModel.getPathToRoot(node);
        return new TreePath(nodes);
    }

    private DefaultMutableTreeNode findOrCreateNode(FileNodeType type, FileNodeData data, DefaultMutableTreeNode parentNode) {

        Optional<DefaultMutableTreeNode> searchResult = lookForNode(type, data, parentNode);

        return searchResult.orElseGet(() -> insertNewNode(type, data, parentNode));
    }

    private DefaultMutableTreeNode insertNewNode(FileNodeType type, FileNodeData data, DefaultMutableTreeNode parentNode) {

        FileNode fileNode = new FileNode(type, data);
        DefaultMutableTreeNode newModelNode = new DefaultMutableTreeNode(fileNode);
        int index = getInsertionIndex(type, data, parentNode);
        treeModel.insertNodeInto(newModelNode, parentNode, index);
        return newModelNode;
    }

    private int getInsertionIndex(FileNodeType type, FileNodeData data, DefaultMutableTreeNode parentNode) {

        int count = parentNode.getChildCount();

        if (type != FileNodeType.IMAGE) {
            return count;
        }
        if (data.getInstanceNumber().isEmpty()) {
            return count;
        }


        for (int i = 0; i < count; i++) {

            DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) parentNode.getChildAt(i);

            if (currentNode.getUserObject() instanceof FileNode fileNode) {
                if (fileNode.getData().getInstanceNumber().isPresent()) {

                    int instanceNumber = data.getInstanceNumber().get();
                    int currentInstanceNumber = fileNode.getData().getInstanceNumber().get();

                    if (currentInstanceNumber > instanceNumber) {
                        return i;
                    }

                }
            }
        }

        return count;
    }

    private Optional<DefaultMutableTreeNode> lookForNode(FileNodeType type, FileNodeData data, DefaultMutableTreeNode parentNode) {

        Enumeration<TreeNode> e = parentNode.children();

        while (e.hasMoreElements()) {

            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();

            boolean found = doesNodeMatch(type, data, node);
            if (found) {
                return Optional.of(node);
            }
        }

        return Optional.empty();
    }

    private boolean doesNodeMatch(FileNodeType type, FileNodeData data, DefaultMutableTreeNode analyzedNode) {

        if (!(analyzedNode.getUserObject() instanceof FileNode fileNode)) {
            return false;
        }

        return nodeHasMatchingId(type, data, fileNode) && nodeHasMatchingType(type, fileNode);
    }

    private boolean nodeHasMatchingId(FileNodeType type, FileNodeData data, FileNode analyzedNode) {

        return switch (type) {
            case PATIENT, STUDY -> nodeHasMatchingStudyInstanceUid(data, analyzedNode);
            case SERIES -> nodeHasMatchingSeriesInstanceUid(data, analyzedNode);
            default -> false;
        };

    }

    private boolean nodeHasMatchingType(FileNodeType type, FileNode analyzedNode) {

        FileNodeType nodeType = analyzedNode.getType();
        return nodeType.equals(type);
    }

    private boolean nodeHasMatchingStudyInstanceUid(FileNodeData data, FileNode fileNode) {

        Optional<String> nodeUid = fileNode.getData().getStudyInstanceUid();
        Optional<String> studyInstanceUid = data.getStudyInstanceUid();

        if (nodeUid.isEmpty() || studyInstanceUid.isEmpty()) {
            return false;
        }

        return nodeUid.get().equals(studyInstanceUid.get());
    }

    private boolean nodeHasMatchingSeriesInstanceUid(FileNodeData data, FileNode fileNode) {

        Optional<String> nodeUid = fileNode.getData().getSeriesInstanceUid();
        Optional<String> seriesInstanceUid = data.getSeriesInstanceUid();

        if (nodeUid.isEmpty() || seriesInstanceUid.isEmpty()) {
            return false;
        }

        return nodeUid.get().equals(seriesInstanceUid.get());
    }

}
