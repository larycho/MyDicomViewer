package org.example.mydicomviewer.processing.dicomdir;

import org.example.mydicomviewer.models.DicomDirectory;

import java.io.File;

public interface DicomDirProcessor {
    public DicomDirectory openDicomDirectory(File file);
}
