package org.mydicomviewer.ui.image.shapes;

import org.mydicomviewer.tools.shapes.DrawableShape;

import java.util.List;
import java.util.Map;

public interface ShapeManager {

    boolean areShapesPersisted();
    void setPersisted(boolean persisted);

    List<DrawableShape> getShapesForFrame(int frame);
    Map<Integer, List<DrawableShape>> getAllShapes();

    void addShape(int frame, DrawableShape shape);
    void addShapes(int frame, List<DrawableShape> shapes);
    void removeShape(int frame, DrawableShape shape);
    void clearShapes();

}
