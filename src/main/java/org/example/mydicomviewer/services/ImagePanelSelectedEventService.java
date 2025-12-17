package org.example.mydicomviewer.services;

import org.example.mydicomviewer.events.PanelSelectedEvent;
import org.example.mydicomviewer.listeners.PanelSelectedListener;

public interface ImagePanelSelectedEventService {
    void addListener(PanelSelectedListener listener);
    void notifyListeners(PanelSelectedEvent event);
}
