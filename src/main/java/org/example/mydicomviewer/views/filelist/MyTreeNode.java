package org.example.mydicomviewer.views.filelist;


import org.example.mydicomviewer.models.DicomFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MyTreeNode {
    private String type;
    private String text;
    private List<MyTreeNode> children = new ArrayList<MyTreeNode>();
    private boolean isLeaf = false;
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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    public List<MyTreeNode> getChildren() {
        return children;
    }
    public void addChild(MyTreeNode child) {
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
        switch (type) {
            case "PATIENT":
                base = "Patient ID: ";
                break;
            case "STUDY":
                base = "Study ID: ";
                break;
            case "SERIES":
                base = "Series number: ";
                break;
            default:
                break;
        }

        return base += text;
    }
}
