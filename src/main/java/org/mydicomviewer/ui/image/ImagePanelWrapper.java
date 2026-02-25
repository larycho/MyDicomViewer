package org.mydicomviewer.ui.image;

import org.mydicomviewer.processing.overlay.OverlayText;
import org.mydicomviewer.processing.spatial.Axis;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.tools.shapes.DrawableShape;
import org.mydicomviewer.events.services.ImagePanelSelectedEventService;
import org.mydicomviewer.tools.DrawingTool;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public interface ImagePanelWrapper {

    void setImagePanel(ImagePanel imagePanel);

    int getCurrentFrameNumber();

    JComponent getPanel();

    void moveToNextFrame();

    void moveToPreviousFrame();

    void setWindowing(int level, int width);

    void resetWindowing();

    int getWindowLevel();

    int getWindowWidth();

    void setDrawingTool(DrawingTool drawingTool);

    void setPanZoomTool();

    void setWindowingTool();

    void refresh();

    void toggleOverlay();

    OverlayText getOverlay();

    void showSelected();

    void showDeselected();

    DicomFile getDicomFile();

    Map<Integer, List<DrawableShape>> getAllShapes();

    void switchPersistShapes();

    void setPersistShapes(boolean persistShapes);

    boolean areShapesPersisted();

    int getNumberOfFrames();

    void moveToFrame(int frameIndex);

    void setAxis(Axis axis);

    Axis getAxis();

    void displayDefaultImage();

    void addPanelSelectedService(ImagePanelSelectedEventService panelSelectedService);

    void centerImage();
}
