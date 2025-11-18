package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.events.FileLoadStartedEvent;
import org.example.mydicomviewer.events.FileLoadedEvent;

import java.util.EventListener;

public interface FileLoadStartedListener extends EventListener {
    void loadStarted(FileLoadStartedEvent event);
}
