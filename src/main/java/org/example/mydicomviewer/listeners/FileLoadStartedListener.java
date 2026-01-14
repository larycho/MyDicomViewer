package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.events.FileLoadStartedEvent;

import java.util.EventListener;

public interface FileLoadStartedListener extends EventListener {
    void loadStarted(FileLoadStartedEvent event);
}
