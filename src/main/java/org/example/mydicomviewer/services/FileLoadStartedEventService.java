package org.example.mydicomviewer.services;

import org.example.mydicomviewer.listeners.FileLoadStartedListener;
import org.example.mydicomviewer.listeners.FileLoadedListener;
import org.example.mydicomviewer.models.DicomFile;

import java.io.File;

public interface FileLoadStartedEventService {
    void addListener(FileLoadStartedListener listener);
    void removeListener(FileLoadStartedListener listener);
    void notifyStarted(File file);
}
