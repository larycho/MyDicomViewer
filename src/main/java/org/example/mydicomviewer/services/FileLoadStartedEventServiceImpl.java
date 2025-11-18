package org.example.mydicomviewer.services;

import com.google.inject.Singleton;
import org.example.mydicomviewer.events.FileLoadStartedEvent;
import org.example.mydicomviewer.listeners.FileLoadStartedListener;

import java.io.File;
import java.util.ArrayList;

@Singleton
public class FileLoadStartedEventServiceImpl implements FileLoadStartedEventService {

    private final ArrayList<FileLoadStartedListener> listeners = new ArrayList<>();

    @Override
    public void addListener(FileLoadStartedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(FileLoadStartedListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyStarted(File file) {
        FileLoadStartedEvent event = new FileLoadStartedEvent(this, file);
        for (FileLoadStartedListener listener : listeners) {
            listener.loadStarted(event);
        }
    }
}
