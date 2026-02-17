package org.mydicomviewer.events.listeners;

import org.mydicomviewer.events.FileReloadedEvent;

import java.util.EventListener;

public interface FileReloadedListener extends EventListener {
    void fileReloaded(FileReloadedEvent event);
}
