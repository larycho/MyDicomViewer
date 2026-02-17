package org.mydicomviewer.services.implementations;

import org.mydicomviewer.processing.windowing.WindowPreset;
import org.mydicomviewer.services.WindowingPresetProvider;

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

        presets.add(new WindowPreset(80, 40, "Brain (CT)"));
        presets.add(new WindowPreset(1500, -600, "Lungs (CT)"));
        presets.add(new WindowPreset(350, 50, "Mediastinum (CT)"));
        presets.add(new WindowPreset(600, 200, "Heart (CT)"));
        presets.add(new WindowPreset(400, 50, "Abdomen - Soft Tissues (CT)"));
        presets.add(new WindowPreset(150, 30, "Liver (CT)"));
        presets.add(new WindowPreset(250, 50, "Spine - Soft Tissues (CT)"));
        presets.add(new WindowPreset(1800, 400, "Spine - Bone (CT)"));

        return presets;
    }
}
