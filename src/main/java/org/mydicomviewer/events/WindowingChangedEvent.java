package org.mydicomviewer.events;

import java.util.EventObject;

public class WindowingChangedEvent extends EventObject {

    private final int windowCenter;
    private final int windowWidth;

    public WindowingChangedEvent(Object source, int windowCenter, int windowWidth) {
        super(source);
        this.windowCenter = windowCenter;
        this.windowWidth = windowWidth;
    }

    public int getWindowCenter() {
        return windowCenter;
    }

    public int getWindowWidth() {
        return windowWidth;
    }
}
