package org.mydicomviewer.events.listeners;

import org.mydicomviewer.events.FileLoadStartedEvent;

import java.util.EventListener;

public interface FileLoadStartedListener extends EventListener {
    void loadStarted(FileLoadStartedEvent event);
}
