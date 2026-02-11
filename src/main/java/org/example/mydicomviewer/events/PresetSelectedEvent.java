package org.example.mydicomviewer.events;

import org.example.mydicomviewer.processing.image.WindowPreset;

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
