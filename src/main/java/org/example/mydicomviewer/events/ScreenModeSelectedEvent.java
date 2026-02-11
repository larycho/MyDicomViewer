package org.example.mydicomviewer.events;

import org.example.mydicomviewer.display.SplitScreenMode;

import java.util.EventObject;

public class ScreenModeSelectedEvent extends EventObject {

    private final SplitScreenMode mode;

    public ScreenModeSelectedEvent(Object source, SplitScreenMode mode) {
        super(source);
        this.mode = mode;
    }

    public SplitScreenMode getMode() {
        return mode;
    }
}
