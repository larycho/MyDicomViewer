package org.example.mydicomviewer.services;

import org.example.mydicomviewer.listeners.FolderLoadedListener;

import java.io.File;

public interface FolderLoadManager {
    void openFolder(File folder);
}
