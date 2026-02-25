package org.mydicomviewer.ui.image.shapes;

import org.mydicomviewer.tools.shapes.DrawableShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShapeManagerImpl implements ShapeManager {

    private boolean persistShapes = true;

    private final List<DrawableShape> currentShapes = new ArrayList<>();
    private final Map<Integer, List<DrawableShape>> shapesForEachFrame = new HashMap<>();
    private int currentFrame = 0;

    @Override
    public boolean areShapesPersisted() {
        return persistShapes;
    }

    @Override
    public void setPersisted(boolean persisted, int referenceFrame) {
        currentFrame = referenceFrame;
        List<DrawableShape> shapes = new ArrayList<>(getShapesForFrame(currentFrame));
        clearShapes();
        this.persistShapes = persisted;
        clearShapes();
        addShapes(shapes);
    }

    @Override
    public List<DrawableShape> getShapesForFrame(int frame) {
        if (persistShapes) {
            return new ArrayList<>(currentShapes);
        }
        else {
            List<DrawableShape> shapes = shapesForEachFrame.get(frame);
            if (shapes == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(shapes);
        }
    }

    @Override
    public Map<Integer, List<DrawableShape>> getAllShapes() {
        if (persistShapes) {
            Map<Integer, List<DrawableShape>> shapes = new HashMap<>();
            shapes.put(0, new ArrayList<>(currentShapes));
            return shapes;
        }
        else {
            return new HashMap<>(shapesForEachFrame);
        }
    }

    @Override
    public void addShape(int frame, DrawableShape shape) {
        if (persistShapes) {
            currentShapes.add(shape);
        }
        else {
            List<DrawableShape> shapes = shapesForEachFrame.computeIfAbsent(frame, k -> new ArrayList<>());
            shapes.add(shape);
        }
    }

    public void addShape(DrawableShape shape) {
        if (persistShapes) {
            currentShapes.add(shape);
        }
        else {
            List<DrawableShape> shapes = shapesForEachFrame.computeIfAbsent(currentFrame, k -> new ArrayList<>());
            shapes.add(shape);
        }
    }

    public void addShapes(List<DrawableShape> shapes) {
        for (DrawableShape shape : shapes) {
            addShape(shape);
        }
    }
    @Override
    public void addShapes(int frame, List<DrawableShape> shapes) {
        for (DrawableShape shape : shapes) {
            addShape(frame, shape);
        }
    }

    @Override
    public void removeShape(int frame, DrawableShape shape) {
        if (persistShapes) {
            currentShapes.remove(shape);
        }
        else {
            List<DrawableShape> shapes = shapesForEachFrame.get(frame);
            if (shapes != null) {
                shapes.remove(shape);
            }
        }
    }

    @Override
    public void clearShapes() {
        if (persistShapes) {
            currentShapes.clear();
        }
        else {
            shapesForEachFrame.clear();
        }
    }
}
