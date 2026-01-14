package org.example.mydicomviewer.views.filelist;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileTreeNode {
    private String type;
    private FileNodeType nodeType = FileNodeType.IMAGE;
    private String text;
    private List<FileTreeNode> children = new ArrayList<FileTreeNode>();
    private boolean isLeaf = false;
    private boolean isRoot = false;
    private File file;
    private boolean isPartialFile = false;
    private List<File> mainFile;

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public boolean isPartialFile() {
        return isPartialFile;
    }

    public void setPartialFile(boolean partialFile) {
        isPartialFile = partialFile;
    }


    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }

    public boolean isRoot() {
        return isRoot;
    }

    public void setRoot(boolean root) {
        isRoot = root;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public FileNodeType getNodeType() {
        return nodeType;
    }

    public void setNodeType(FileNodeType nodeType) {
        this.nodeType = nodeType;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public List<FileTreeNode> getChildren() {
        return children;
    }
    public void addChild(FileTreeNode child) {
        children.add(child);
    }

    public List<File> getMainFile() {
        return mainFile;
    }

    public void setMainFile(List<File> mainFile) {
        this.mainFile = new ArrayList<>(mainFile);
    }

    @Override
    public String toString() {
        String base = "";
        switch (nodeType) {
            case PATIENT:
                base = "Patient ID: ";
                break;
            case STUDY:
                base = "Study ID: ";
                break;
            case SERIES:
                base = "Series number: ";
                break;
            default:
                break;
        }

        return base += text;
    }
}
