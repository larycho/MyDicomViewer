package org.mydicomviewer.events.services;

import org.mydicomviewer.events.PanelSelectedEvent;
import org.mydicomviewer.events.listeners.PanelSelectedListener;

public interface ImagePanelSelectedEventService {
    void addListener(PanelSelectedListener listener);
    void notifyListeners(PanelSelectedEvent event);
}
