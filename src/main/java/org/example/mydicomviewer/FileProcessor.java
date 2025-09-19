package org.example.mydicomviewer;

import org.example.mydicomviewer.models.DicomFile;

import java.io.File;

public interface FileProcessor {
    public DicomFile readFile(File file);
}
