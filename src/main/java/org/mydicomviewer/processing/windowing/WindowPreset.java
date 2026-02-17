package org.mydicomviewer.processing.windowing;

public class WindowPreset {

    private final int width;
    private final int level;
    private final String name;

    public WindowPreset(int width, int level, String name) {
        this.width = width;
        this.level = level;
        this.name = name;
    }

    public int getWidth() {
        return width;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
