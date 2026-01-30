package org.example;

import org.example.mydicomviewer.views.image.DrawingTool;
import org.example.mydicomviewer.views.image.DrawingToolFactory;

public class RectangleToolFactory implements DrawingToolFactory {

    @Override
    public DrawingTool getTool() {
        return new RectangleTool();
    }

    @Override
    public String toString() {
        return "Rectangle";
    }
}
