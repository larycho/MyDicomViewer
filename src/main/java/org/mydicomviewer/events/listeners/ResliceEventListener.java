package org.mydicomviewer.events.listeners;

import org.mydicomviewer.events.RequestedResliceEvent;

public interface ResliceEventListener {

    void resliceRequested(RequestedResliceEvent event);
}
