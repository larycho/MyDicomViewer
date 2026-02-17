package org.mydicomviewer.events.services;

import org.mydicomviewer.events.FolderLoadedEvent;
import org.mydicomviewer.events.listeners.FolderLoadedListener;

public interface FolderLoadedEventService {
    void notify(FolderLoadedEvent event);
    void addListener(FolderLoadedListener listener);
}
