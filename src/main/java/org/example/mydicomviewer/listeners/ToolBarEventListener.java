package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.events.PresetSelectedEvent;
import org.example.mydicomviewer.events.ScreenModeSelectedEvent;
import org.example.mydicomviewer.events.WindowingChangedEvent;
import org.example.mydicomviewer.events.WindowingResetEvent;

public interface ToolBarEventListener {
    void presetSelected(PresetSelectedEvent event);
    void screenModeSelected(ScreenModeSelectedEvent event);
    void windowingChanged(WindowingChangedEvent event);
    void windowingReset(WindowingResetEvent event);
}
