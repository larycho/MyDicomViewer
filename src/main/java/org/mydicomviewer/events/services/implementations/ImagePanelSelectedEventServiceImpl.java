package org.mydicomviewer.events.services.implementations;

import com.google.inject.Singleton;
import org.mydicomviewer.events.PanelSelectedEvent;
import org.mydicomviewer.events.services.ImagePanelSelectedEventService;
import org.mydicomviewer.events.listeners.PanelSelectedListener;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ImagePanelSelectedEventServiceImpl implements ImagePanelSelectedEventService {

    List<PanelSelectedListener> listeners = new ArrayList<PanelSelectedListener>();
    @Override
    public void addListener(PanelSelectedListener listener) {
        listeners.add(listener);
    }

    @Override
    public void notifyListeners(PanelSelectedEvent event) {
        for (PanelSelectedListener listener : listeners) {
            listener.panelSelected(event);
        }
    }
}
