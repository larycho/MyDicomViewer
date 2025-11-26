package org.example.mydicomviewer.display.overlay;

import java.util.ArrayList;
import java.util.List;

public class OverlayText {

    private final List<String> topLeft;
    private final List<String> topRight;
    private final List<String> bottomLeft;
    private final List<String> bottomRight;

    public OverlayText() {
        topLeft = new ArrayList<>();
        topRight = new ArrayList<>();
        bottomLeft = new ArrayList<>();
        bottomRight = new ArrayList<>();
    }

    public void addTopLeft(String text) {
        topLeft.add(text);
    }
    public void addTopRight(String text) {
        topRight.add(text);
    }
    public void addBottomLeft(String text) {
        bottomLeft.add(text);
    }
    public void addBottomRight(String text) {
        bottomRight.add(text);
    }

    public void clearTopLeft() {
        topLeft.clear();
    }

    public void clearTopRight() {
        topRight.clear();
    }

    public void clearBottomLeft() {
        bottomLeft.clear();
    }

    public void clearBottomRight() {
        bottomRight.clear();
    }

    public String getTopLeft() {
        StringBuilder builder = new StringBuilder();

        for (String s : topLeft) {
            builder.append(s);
            builder.append("\n");
        }

        return builder.toString();
    }

    public String getTopRight() {
        StringBuilder builder = new StringBuilder();

        for (String s : topRight) {
            builder.append(s);
            builder.append("\n");
        }

        return builder.toString();
    }

    public String getBottomLeft() {
        StringBuilder builder = new StringBuilder();

        for (String s : bottomLeft) {
            builder.append(s);
            builder.append("\n");
        }

        return builder.toString();
    }

    public String getBottomRight() {
        StringBuilder builder = new StringBuilder();

        for (String s : bottomRight) {
            builder.append(s);
            builder.append("\n");
        }

        return builder.toString();
    }
}
