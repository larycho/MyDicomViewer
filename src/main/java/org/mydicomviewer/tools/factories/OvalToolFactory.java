package org.mydicomviewer.tools.factories;

import org.mydicomviewer.tools.DrawingTool;
import org.mydicomviewer.tools.OvalTool;

public class OvalToolFactory implements DrawingToolFactory {

    @Override
    public DrawingTool getTool() {
        return new OvalTool();
    }

    @Override
    public String toString() {
        return "Ellipse";
    }
}
