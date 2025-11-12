package org.example.mydicomviewer.display;

import java.util.ArrayList;

public class SplitScreenMode {

    private ArrayList<SplitScreenElement> elements;

    public SplitScreenMode() {
        elements = new ArrayList<>();
    }
    public SplitScreenMode(ArrayList<SplitScreenElement> elements) {
        this.elements = elements;
    }

    public ArrayList<SplitScreenElement> getElements() {
        return elements;
    }

    public void setElements(ArrayList<SplitScreenElement> elements) {
        this.elements = elements;
    }

    public void add(SplitScreenElement element) {
        elements.add(element);
    }

    private int getNumberOfRows() {
        if (elements.isEmpty()) {
            return 0;
        }

        int max = 0;
        for (SplitScreenElement element : elements) {
            int x = element.getX();
            if (x > max) {
                max = x;
            }
        }
        return max + 1;
    }

    private int getNumberOfColumns() {
        if (elements.isEmpty()) {
            return 0;
        }

        int max = 0;
        for (SplitScreenElement element : elements) {
            int y = element.getY();
            if (y > max) {
                max = y;
            }
        }
        return max + 1;
    }

    @Override
    public String toString() {
        return "Columns: " + getNumberOfColumns() + ", Rows: " + getNumberOfRows();
    }
}
