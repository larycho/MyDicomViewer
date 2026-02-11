package org.example.mydicomviewer.services;

import com.google.inject.Singleton;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.events.PresetSelectedEvent;
import org.example.mydicomviewer.events.ScreenModeSelectedEvent;
import org.example.mydicomviewer.events.WindowingChangedEvent;
import org.example.mydicomviewer.events.WindowingResetEvent;
import org.example.mydicomviewer.listeners.ToolBarEventListener;
import org.example.mydicomviewer.processing.image.WindowPreset;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class ToolBarEventServiceImpl implements ToolBarEventService {

    private final List<ToolBarEventListener> listeners = new ArrayList<>();

    @Override
    public void addListener(ToolBarEventListener listener) {
        this.listeners.add(listener);
    }

    @Override
    public void notifyPresetsChanged(WindowPreset preset) {

        PresetSelectedEvent event = new PresetSelectedEvent(this, preset);

        for (ToolBarEventListener listener : listeners) {
            listener.presetSelected(event);
        }
    }

    @Override
    public void notifyWindowingChanged(int windowWidth, int windowCenter) {

        WindowingChangedEvent event = new WindowingChangedEvent(this, windowCenter, windowWidth);

        for (ToolBarEventListener listener : listeners) {
            listener.windowingChanged(event);
        }
    }

    @Override
    public void notifyWindowingReset() {

        WindowingResetEvent event = new WindowingResetEvent(this);

        for (ToolBarEventListener listener : listeners) {
            listener.windowingReset(event);
        }
    }

    @Override
    public void notifyScreenModeChanged(SplitScreenMode mode) {

        ScreenModeSelectedEvent event = new ScreenModeSelectedEvent(this, mode);

        for (ToolBarEventListener listener : listeners) {
            listener.screenModeSelected(event);
        }
    }
}
