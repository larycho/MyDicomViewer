package org.example.mydicomviewer.views.image;

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
