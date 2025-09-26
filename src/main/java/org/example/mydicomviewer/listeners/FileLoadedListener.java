package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.events.FileLoadedEvent;

import java.util.EventListener;

public interface FileLoadedListener extends EventListener {
    void fileLoaded(FileLoadedEvent event);
}
