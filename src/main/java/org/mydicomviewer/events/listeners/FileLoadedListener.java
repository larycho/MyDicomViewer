package org.mydicomviewer.events.listeners;

import org.mydicomviewer.events.FileLoadedEvent;

import java.util.EventListener;

public interface FileLoadedListener extends EventListener {
    void fileLoaded(FileLoadedEvent event);
}
