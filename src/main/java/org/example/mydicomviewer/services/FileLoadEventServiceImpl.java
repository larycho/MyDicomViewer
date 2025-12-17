package org.example.mydicomviewer.services;

import com.google.inject.Singleton;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.listeners.FileLoadedListener;
import org.example.mydicomviewer.models.DicomFile;

import java.util.ArrayList;

@Singleton
public class FileLoadEventServiceImpl implements FileLoadEventService {

    private final ArrayList<FileLoadedListener> listeners;

    public FileLoadEventServiceImpl() {
        listeners = new ArrayList<>();
    }

    @Override
    public void addListener(FileLoadedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(FileLoadedListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyFinished(DicomFile file) {
        FileLoadedEvent event = new FileLoadedEvent(this, file);
        for (FileLoadedListener listener : listeners) {
            listener.fileLoaded(event);
        }
    }

    @Override
    public void notifyFinished(DicomFile file, Object source) {
        FileLoadedEvent event = new FileLoadedEvent(source, file);
        for (FileLoadedListener listener : listeners) {
            listener.fileLoaded(event);
        }
    }
}
