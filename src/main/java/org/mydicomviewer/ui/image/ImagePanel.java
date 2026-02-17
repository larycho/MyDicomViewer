package org.mydicomviewer.ui.image;

import org.mydicomviewer.processing.spatial.Axis;
import org.mydicomviewer.tools.shapes.DrawableShape;
import org.mydicomviewer.events.services.ImagePanelSelectedEventService;
import org.mydicomviewer.tools.DrawingTool;
import org.mydicomviewer.tools.ImageTool;

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
