package org.example.mydicomviewer.services;

import org.example.mydicomviewer.processing.image.WindowPreset;

import java.util.ArrayList;
import java.util.List;

public class WindowingPresetProviderImpl implements WindowingPresetProvider {

    private final List<WindowPreset> presets;

    public WindowingPresetProviderImpl() {
        presets = lookForPresets();
    }

    @Override
    public List<WindowPreset> getAvailablePresets() {
        return presets;
    }

    private List<WindowPreset> lookForPresets() {
        List<WindowPreset> presets = new ArrayList<>();

        presets.add(new WindowPreset(80, 40, "Brain"));
        presets.add(new WindowPreset(1500, -600, "Lungs"));
        presets.add(new WindowPreset(350, 50, "Mediastinum"));
        presets.add(new WindowPreset(600, 200, "Heart"));
        presets.add(new WindowPreset(400, 50, "Abdomen - Soft Tissues"));
        presets.add(new WindowPreset(150, 30, "Liver"));
        presets.add(new WindowPreset(250, 50, "Spine - Soft Tissues"));
        presets.add(new WindowPreset(1800, 400, "Spine - Bone"));

        return presets;
    }
}
