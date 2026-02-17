package org.mydicomviewer.events;

import org.mydicomviewer.processing.windowing.WindowPreset;

import java.util.EventObject;

public class PresetSelectedEvent extends EventObject {

    private final WindowPreset preset;

    public PresetSelectedEvent(Object source, WindowPreset preset) {
        super(source);
        this.preset = preset;
    }

    public WindowPreset getPreset() {
        return preset;
    }
}
