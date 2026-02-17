package org.mydicomviewer.processing.io.file;

import org.mydicomviewer.models.DicomFile;

import java.io.File;

public interface FileProcessor {
    DicomFile readFile(File file);
}
