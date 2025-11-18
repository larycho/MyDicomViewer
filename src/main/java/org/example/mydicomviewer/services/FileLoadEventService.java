package org.example.mydicomviewer.services;

import org.example.mydicomviewer.listeners.FileLoadedListener;
import org.example.mydicomviewer.models.DicomFile;

import java.io.File;

public interface FileLoadEventService {

    void addListener(FileLoadedListener listener);
    void removeListener(FileLoadedListener listener);
    void notifyFinished(DicomFile file);
}
