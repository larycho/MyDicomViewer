package org.example;

import org.mydicomviewer.tools.DrawingTool;
import org.mydicomviewer.tools.factories.DrawingToolFactory;

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
