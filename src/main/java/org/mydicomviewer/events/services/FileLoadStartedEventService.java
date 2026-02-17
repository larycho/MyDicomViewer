package org.mydicomviewer.events.services;

import org.mydicomviewer.events.listeners.FileLoadStartedListener;

import java.io.File;

public interface FileLoadStartedEventService {
    void addListener(FileLoadStartedListener listener);
    void notifyStarted(File file);
}
