package org.mydicomviewer.events.services.implementations;

import com.google.inject.Singleton;
import org.mydicomviewer.events.FileLoadedEvent;
import org.mydicomviewer.events.services.FileLoadEventService;
import org.mydicomviewer.events.listeners.FileLoadedListener;
import org.mydicomviewer.models.DicomFile;

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
