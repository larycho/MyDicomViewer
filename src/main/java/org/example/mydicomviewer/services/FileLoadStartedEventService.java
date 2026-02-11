package org.example.mydicomviewer.services;

import org.example.mydicomviewer.listeners.FileLoadStartedListener;

import java.io.File;

public interface FileLoadStartedEventService {
    void addListener(FileLoadStartedListener listener);
    void notifyStarted(File file);
}
