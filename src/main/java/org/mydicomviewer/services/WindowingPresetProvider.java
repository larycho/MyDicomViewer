package org.mydicomviewer.services;

import org.mydicomviewer.processing.windowing.WindowPreset;

import java.util.List;

public interface WindowingPresetProvider {
    List<WindowPreset> getAvailablePresets();
}
