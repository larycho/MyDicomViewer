package org.mydicomviewer.events.services;

import org.mydicomviewer.events.listeners.ResliceEventListener;

public interface ResliceEventService {

    void addListener(ResliceEventListener listener);
    void notifyResliceRequested();
}
