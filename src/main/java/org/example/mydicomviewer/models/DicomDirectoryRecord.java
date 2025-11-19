package org.example.mydicomviewer.models;

import java.util.ArrayList;
import java.util.List;

public class DicomDirectoryRecord {

    private String type;
    private String text;
    private DicomDirectory mainDicomDirectory;

    private List<DicomDirectoryRecord> children = new ArrayList<DicomDirectoryRecord>();

    public DicomDirectoryRecord(String text) {
        this.text = text;
    }

    public DicomDirectoryRecord(String type, String text) {
        this.type = type;
        this.text = text;
    }

    public DicomDirectoryRecord(String type, String text, DicomDirectory mainDicomDirectory) {
        this.type = type;
        this.text = text;
        this.mainDicomDirectory = mainDicomDirectory;
    }

    public DicomDirectory getMainDicomDirectory() {
        return mainDicomDirectory;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<DicomDirectoryRecord> getChildren() {
        return children;
    }

    public void addChild(DicomDirectoryRecord child) {
        children.add(child);
    }

    public void addChildren(List<DicomDirectoryRecord> children) {
        this.children.addAll(children);
    }

    @Override
    public String toString() {
        String modifiedType = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
        return modifiedType + ": " + text;
    }
}
