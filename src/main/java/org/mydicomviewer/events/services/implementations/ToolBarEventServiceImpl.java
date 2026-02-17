package org.mydicomviewer.events.services.implementations;

import com.google.inject.Singleton;
import org.mydicomviewer.ui.display.SplitScreenMode;
import org.mydicomviewer.events.PresetSelectedEvent;
import org.mydicomviewer.events.ScreenModeSelectedEvent;
import org.mydicomviewer.events.WindowingChangedEvent;
import org.mydicomviewer.events.WindowingResetEvent;
import org.mydicomviewer.events.services.ToolBarEventService;
import org.mydicomviewer.events.listeners.ToolBarEventListener;
import org.mydicomviewer.processing.windowing.WindowPreset;

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
