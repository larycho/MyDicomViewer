package org.example.mydicomviewer.services;

import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.listeners.FolderLoadedListener;

public interface FolderLoadedEventService {
    void notify(FolderLoadedEvent event);
    void addListener(FolderLoadedListener listener);
}
