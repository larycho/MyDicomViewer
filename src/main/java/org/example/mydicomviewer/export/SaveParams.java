package org.example.mydicomviewer.export;

import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.shapes.DrawableShape;

import java.io.File;
import java.util.List;

public class SaveParams {

    private SaveFormat format;
    private List<DicomFile> files;
    private File targetDirectory;
    private List<DrawableShape> shapes;

    public void setFormat(SaveFormat format) {
        this.format = format;
    }
    public SaveFormat getFormat() {
        return format;
    }
    public void setFiles(List<DicomFile> files) {
        this.files = files;
    }
    public List<DicomFile> getFiles() {
        return files;
    }
    public void setTargetDirectory(File targetDirectory) {
        this.targetDirectory = targetDirectory;
    }
    public File getTargetDirectory() {
        return targetDirectory;
    }
    public void setShapes(List<DrawableShape> shapes) {
        this.shapes = shapes;
    }
    public List<DrawableShape> getShapes() {
        return shapes;
    }

    public void validate() {
        if (format == null) {
            throw new IllegalArgumentException("Save format must be specified");
        }
        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("No files to save");
        }
        if (targetDirectory == null) {
            throw new IllegalArgumentException("Target directory must be specified");
        }
        if (targetDirectory.exists() && !targetDirectory.isDirectory()) {
            throw new IllegalArgumentException("Target directory must be a directory");
        }
    }
}
