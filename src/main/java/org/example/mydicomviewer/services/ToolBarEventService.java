package org.example.mydicomviewer.services;

import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.listeners.ToolBarEventListener;
import org.example.mydicomviewer.processing.image.WindowPreset;

public interface ToolBarEventService {

    void addListener(ToolBarEventListener listener);
    void notifyPresetsChanged(WindowPreset preset);
    void notifyWindowingChanged(int windowWidth, int windowCenter);
    void notifyWindowingReset();
    void notifyScreenModeChanged(SplitScreenMode mode);
}
