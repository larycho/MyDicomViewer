package org.example.mydicomviewer.services;

import org.example.mydicomviewer.display.SplitScreenMode;

import java.util.List;

public interface ScreenModeProvider {

    List<SplitScreenMode> getAvailableScreenModes();

    void addScreenMode(SplitScreenMode mode);

    void removeScreenMode(SplitScreenMode mode);

}
