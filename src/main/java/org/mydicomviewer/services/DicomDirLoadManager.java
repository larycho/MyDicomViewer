package org.mydicomviewer.services;

import org.mydicomviewer.events.listeners.DicomDirLoadedListener;

import java.io.File;

public interface DicomDirLoadManager {
    void openDicomDir(File file);
    void addListener(DicomDirLoadedListener listener);
}
