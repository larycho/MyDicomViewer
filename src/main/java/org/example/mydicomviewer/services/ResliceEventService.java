package org.example.mydicomviewer.services;

import org.example.mydicomviewer.listeners.ResliceEventListener;

public interface ResliceEventService {

    void addListener(ResliceEventListener listener);
    void notifyResliceRequested();
}
