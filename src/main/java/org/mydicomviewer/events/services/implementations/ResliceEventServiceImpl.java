package org.mydicomviewer.events.services.implementations;

import com.google.inject.Singleton;
import org.mydicomviewer.events.RequestedResliceEvent;
import org.mydicomviewer.events.services.ResliceEventService;
import org.mydicomviewer.events.listeners.ResliceEventListener;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ResliceEventServiceImpl implements ResliceEventService {

    private final List<ResliceEventListener> listeners = new ArrayList<>();

    @Override
    public void addListener(ResliceEventListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void notifyResliceRequested() {
        RequestedResliceEvent event = new RequestedResliceEvent(this);

        for (ResliceEventListener listener : listeners) {
            listener.resliceRequested(event);
        }
    }
}
