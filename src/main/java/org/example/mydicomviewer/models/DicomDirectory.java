package org.example.mydicomviewer.models;

import java.io.File;

public class DicomDirectory {

    private File directory;
    private DicomDirectoryRecord root;

    public DicomDirectory(File directory, DicomDirectoryRecord root) {
        this.directory = directory;
        this.root = root;
    }

    public File getDirectory() {
        return directory;
    }

    public DicomDirectoryRecord getRoot() {
        return root;
    }
}
