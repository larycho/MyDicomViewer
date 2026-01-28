package org.example.mydicomviewer.views.image.panel;

import org.example.mydicomviewer.models.shapes.DrawableShape;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;
import org.example.mydicomviewer.views.image.DrawingTool;
import org.example.mydicomviewer.views.image.ImageTool;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ImagePanel {

    void setImage(BufferedImage image);

    void setShapes(List<DrawableShape> shapes);

    void setImageTool(ImageTool imageTool);

    void setDrawingTool(DrawingTool drawingTool);

    void refresh();

    void toggleOverlay();

    void select();

    void deselect();

    void addPanelSelectedService(ImagePanelSelectedEventService panelSelectedService);

    void setPanZoomTool();

    void setWindowingTool();

    void setAxis(Axis axis);

    void centerImage();

    void setFrameNumber(int frameIndex);
}
