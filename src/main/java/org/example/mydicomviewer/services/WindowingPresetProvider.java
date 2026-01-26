package org.example.mydicomviewer.services;

import org.example.mydicomviewer.processing.image.WindowPreset;

import java.util.List;

public interface WindowingPresetProvider {
    List<WindowPreset> getAvailablePresets();
}
