package org.mydicomviewer.events.services.implementations;

import com.google.inject.Singleton;
import org.mydicomviewer.events.FolderLoadedEvent;
import org.mydicomviewer.events.services.FolderLoadedEventService;
import org.mydicomviewer.events.listeners.FolderLoadedListener;

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
