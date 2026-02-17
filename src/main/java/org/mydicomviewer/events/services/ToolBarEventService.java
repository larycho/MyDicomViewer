package org.mydicomviewer.events.services;

import org.mydicomviewer.ui.display.SplitScreenMode;
import org.mydicomviewer.events.listeners.ToolBarEventListener;
import org.mydicomviewer.processing.windowing.WindowPreset;

public interface ToolBarEventService {

    void addListener(ToolBarEventListener listener);
    void notifyPresetsChanged(WindowPreset preset);
    void notifyWindowingChanged(int windowWidth, int windowCenter);
    void notifyWindowingReset();
    void notifyScreenModeChanged(SplitScreenMode mode);
}
