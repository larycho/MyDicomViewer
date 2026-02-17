package org.mydicomviewer.events.services.implementations;

import com.google.inject.Singleton;
import org.mydicomviewer.events.FileLoadStartedEvent;
import org.mydicomviewer.events.services.FileLoadStartedEventService;
import org.mydicomviewer.events.listeners.FileLoadStartedListener;

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
    public void notifyStarted(File file) {
        FileLoadStartedEvent event = new FileLoadStartedEvent(this, file);
        for (FileLoadStartedListener listener : listeners) {
            listener.loadStarted(event);
        }
    }
}
