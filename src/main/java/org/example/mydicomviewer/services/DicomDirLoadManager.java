package org.example.mydicomviewer.services;

import org.example.mydicomviewer.listeners.DicomDirLoadedListener;

import java.io.File;

public interface DicomDirLoadManager {
    void openDicomDir(File file);
    void addListener(DicomDirLoadedListener listener);
}
