package org.example.mydicomviewer.services;

import org.example.mydicomviewer.events.FileReloadedEvent;
import org.example.mydicomviewer.listeners.FileReloadedListener;
import org.example.mydicomviewer.models.DicomFile;

import java.io.File;
import java.util.ArrayList;

public class FileReloadEventServiceImpl implements FileReloadEventService {

    private final ArrayList<FileReloadedListener> listeners = new ArrayList<>();

    @Override
    public void addListener(FileReloadedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyReloaded(File file) {
        FileReloadedEvent event = new FileReloadedEvent(this, file);
        for (FileReloadedListener listener : listeners) {
            listener.fileReloaded(event);
        }
    }
}
