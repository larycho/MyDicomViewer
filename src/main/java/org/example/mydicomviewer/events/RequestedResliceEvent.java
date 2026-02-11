package org.example.mydicomviewer.events;

import java.util.EventObject;

public class RequestedResliceEvent extends EventObject {

    public RequestedResliceEvent(Object source) {
        super(source);
    }
}
