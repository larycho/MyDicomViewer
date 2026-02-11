package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.events.RequestedResliceEvent;

public interface ResliceEventListener {

    void resliceRequested(RequestedResliceEvent event);
}
