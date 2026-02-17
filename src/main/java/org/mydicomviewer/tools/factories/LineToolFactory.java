package org.mydicomviewer.tools.factories;

import org.mydicomviewer.tools.DrawingTool;
import org.mydicomviewer.tools.LineTool;

public class LineToolFactory implements DrawingToolFactory {

    @Override
    public DrawingTool getTool() {
        return new LineTool();
    }

    @Override
    public String toString() {
        return "Straight Line";
    }
}
