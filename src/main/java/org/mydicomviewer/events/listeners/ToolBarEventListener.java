package org.mydicomviewer.events.listeners;

import org.mydicomviewer.events.PresetSelectedEvent;
import org.mydicomviewer.events.ScreenModeSelectedEvent;
import org.mydicomviewer.events.WindowingChangedEvent;
import org.mydicomviewer.events.WindowingResetEvent;

public interface ToolBarEventListener {
    void presetSelected(PresetSelectedEvent event);
    void screenModeSelected(ScreenModeSelectedEvent event);
    void windowingChanged(WindowingChangedEvent event);
    void windowingReset(WindowingResetEvent event);
}
