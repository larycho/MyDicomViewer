package org.mydicomviewer.ui.image;

import org.mydicomviewer.tools.shapes.DrawableShape;
import org.mydicomviewer.tools.DrawingTool;
import org.mydicomviewer.tools.ImageTool;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public interface InnerImagePanel {

    void setImage(BufferedImage image);
    void setShapes(List<DrawableShape> shapes);

    void addShape(DrawableShape shape);

    void setImageTool(ImageTool imageTool);
    void setDrawingTool(DrawingTool drawingTool);
    void toggleOverlay();

    double getZoom();

    void setZoom(double zoom);

    void setOffset(Point p);

    Point getOffset();

    void refresh();

    double getDistance(Point2D.Double p1, Point2D.Double p2);

    boolean isDistanceValid();

    void centerImage();

    AffineTransform getTransform();
}
