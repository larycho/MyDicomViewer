package org.mydicomviewer.events.listeners;

import org.mydicomviewer.events.FrameSkipEvent;

public interface FrameSkipEventListener {
    void frameSkipped(FrameSkipEvent event);
}
