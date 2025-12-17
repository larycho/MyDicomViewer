package org.example.mydicomviewer.services;

import com.google.inject.Singleton;
import org.example.mydicomviewer.events.FolderLoadedEvent;
import org.example.mydicomviewer.listeners.FolderLoadedListener;

import java.util.ArrayList;

@Singleton
public class FolderLoadedEventServiceImpl implements FolderLoadedEventService {

    private final ArrayList<FolderLoadedListener> listeners = new ArrayList<>();

    @Override
    public void notify(FolderLoadedEvent event) {
        for (FolderLoadedListener listener : listeners) {
            listener.folderLoaded(event);
        }
    }

    @Override
    public void addListener(FolderLoadedListener listener) {
        listeners.add(listener);
    }
}
