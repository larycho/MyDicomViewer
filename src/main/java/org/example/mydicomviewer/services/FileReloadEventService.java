package org.example.mydicomviewer.services;

import org.example.mydicomviewer.listeners.FileReloadedListener;

import java.io.File;

public interface FileReloadEventService {
    void addListener(FileReloadedListener listener);
    void notifyReloaded(File file);
}
