package org.example.mydicomviewer.views.image;

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
