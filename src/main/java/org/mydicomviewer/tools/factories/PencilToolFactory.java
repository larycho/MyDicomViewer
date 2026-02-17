package org.mydicomviewer.tools.factories;

import org.mydicomviewer.tools.DrawingTool;
import org.mydicomviewer.tools.PencilTool;

public class PencilToolFactory implements DrawingToolFactory {

    @Override
    public DrawingTool getTool() {
        return new PencilTool();
    }

    @Override
    public String toString() {
        return "Pencil";
    }
}
