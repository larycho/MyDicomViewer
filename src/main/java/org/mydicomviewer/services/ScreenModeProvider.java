package org.mydicomviewer.services;

import org.mydicomviewer.ui.display.SplitScreenMode;

import java.util.List;

public interface ScreenModeProvider {

    List<SplitScreenMode> getAvailableScreenModes();

    SplitScreenMode getDefaultScreenMode();

}
