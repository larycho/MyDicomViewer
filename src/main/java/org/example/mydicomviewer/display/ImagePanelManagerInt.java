package org.example.mydicomviewer.display;

import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.shapes.DrawableShape;

import java.awt.image.BufferedImage;
import java.util.List;

public interface ImagePanelManagerInt {

    public OverlayText getOverlay();

    DicomFile getDicomFile();

    public int getWindowLevel();

    public int getWindowWidth();

    public void setWindowLevel(int windowLevel);

    public void setWindowWidth(int windowWidth);

    public BufferedImage moveToNextFrame();

    public BufferedImage moveToPreviousFrame();
    //hm?
    public void setCurrentFrameNumber(int currentFrame);
    // hm
    public void changeWindowLevelBy(int delta);

    public void changeWindowWidthBy(int delta);

    public BufferedImage getFrameForDisplay();

    void setPersistShapesBetweenFrames(boolean persistShapes);

    void addShape(DrawableShape line);

    void addShapes(List<DrawableShape> shapes);

    void deleteShape(DrawableShape line);

    void clearShapes();

    List<DrawableShape> getCurrentShapes();

    boolean areShapesPersisted();

    int getCurrentFrameNumber();
}
