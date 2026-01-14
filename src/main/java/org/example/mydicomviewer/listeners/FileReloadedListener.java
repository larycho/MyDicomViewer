package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.events.FileReloadedEvent;

import java.util.EventListener;

public interface FileReloadedListener extends EventListener {
    void fileReloaded(FileReloadedEvent event);
}
