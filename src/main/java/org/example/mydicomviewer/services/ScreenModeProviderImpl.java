package org.example.mydicomviewer.services;

import org.example.mydicomviewer.display.SplitScreenElement;
import org.example.mydicomviewer.display.SplitScreenMode;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScreenModeProviderImpl implements ScreenModeProvider {

    private ArrayList<SplitScreenMode> modes;

    public ScreenModeProviderImpl() {
        modes = searchForScreenModes();
    }

    @Override
    public List<SplitScreenMode> getAvailableScreenModes() {
        return modes;
    }

    @Override
    public void addScreenMode(SplitScreenMode mode) {
        modes.add(mode);
    }

    @Override
    public void removeScreenMode(SplitScreenMode mode) {
        modes.remove(mode);
    }

    // This is a temporary method - the end goal is to read this data from file
    private ArrayList<SplitScreenMode> searchForScreenModes() {
        ArrayList<SplitScreenMode> modes = new ArrayList<>();

        // Single image
        modes.add(createScreenMode(List.of(new Point(0, 0))));
        // Two images next to each other
        modes.add(createScreenMode(Arrays.asList(new Point(0,0), new Point(0,1))));
        // Two images, one on top of the other
        modes.add(createScreenMode(Arrays.asList(new Point(0,0), new Point(1,0))));
        // Four images in a 2x2 grid
        modes.add(createScreenMode(Arrays.asList(new Point(0,0), new Point(1,0), new Point(0,1), new Point(1,1))));

        return modes;
    }

    private SplitScreenMode createScreenMode(List<Point> points) {
        ArrayList<SplitScreenElement> elements = new ArrayList<>();

        for (Point point : points) {
            SplitScreenElement element = new SplitScreenElement(point.x, point.y);
            elements.add(element);
        }

        return new SplitScreenMode(elements);
    }

}
