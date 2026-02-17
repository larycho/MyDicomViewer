package org.mydicomviewer.ui.display;

public class SplitScreenElement {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final int DEFAULT_WIDTH = 1;
    private final int DEFAULT_HEIGHT = 1;

    public SplitScreenElement(int x, int y) {
        this.x = x;
        this.y = y;
        this.width = DEFAULT_WIDTH;
        this.height = DEFAULT_HEIGHT;
    }

    public SplitScreenElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
