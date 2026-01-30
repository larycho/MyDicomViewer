package org.example.mydicomviewer.views.image;

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
