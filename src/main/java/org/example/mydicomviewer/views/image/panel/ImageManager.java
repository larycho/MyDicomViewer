package org.example.mydicomviewer.views.image.panel;

import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.shapes.DrawableShape;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public interface ImageManager {

    DicomFile getDicomFile();

    BufferedImage getImageWithWindowing(int level, int width);

    int getWindowWidth();

    void setWindowWidth(int width);

    int getWindowLevel();

    void setWindowLevel(int windowLevel);

    void changeWindowLevelBy(int delta);

    void changeWindowWidthBy(int delta);

    void resetWindowing();

    BufferedImage moveToNextFrame();

    BufferedImage moveToPreviousFrame();

    void setCurrentFrameNumber(int frameNumber);

    int getCurrentFrameNumber();

    BufferedImage getCurrentFrame();

    void setPersistShapes(boolean persistShapes);

    boolean areShapesPersisted();

    void addShape(DrawableShape shape);

    void addShapes(List<DrawableShape> shapes);

    void removeShape(DrawableShape shape);

    void clearShapes();

    List<DrawableShape> getShapesForCurrentFrame();

    Map<Integer, List<DrawableShape>> getAllShapes();

    OverlayText getOverlay();

    int getNumberOfFrames();

    void setAxis(Axis axis);

    Axis getAxis();

    double getDistance(Point2D.Double p1, Point2D.Double p2);

    double getAspectRatioShift();
}
