package org.mydicomviewer.processing.io.export;

import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.tools.shapes.DrawableShape;

import java.io.File;
import java.util.List;

public class SaveParams {

    private SaveFormat format;
    private List<DicomFile> files;
    private File targetDirectory;
    private List<DrawableShape> shapes;
    private boolean singleFrame = false;
    private int frameNumber = 0;

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
    public boolean isSingleFrame() {
        return singleFrame;
    }
    public void setSingleFrame(boolean singleFrame) {
        this.singleFrame = singleFrame;
    }
    public int getFrameNumber() {
        return frameNumber;
    }
    public void setFrameNumber(int frameNumber) {
        this.frameNumber = frameNumber;
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
