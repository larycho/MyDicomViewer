package org.example.mydicomviewer.views.image.panel;

import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.shapes.DrawableShape;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;
import org.example.mydicomviewer.views.image.DrawingTool;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public interface ImagePanelWrapper {

    void setImagePanel(ImagePanel imagePanel);

    int getCurrentFrameNumber();

    JPanel getPanel();

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

    void setPersistShapes(boolean persistFrames);

    boolean areShapesPersisted();

    int getNumberOfFrames();

    void moveToFrame(int frameIndex);

    void setAxis(Axis axis);

    Axis getAxis();

    void displayDefaultImage();

    void addPanelSelectedService(ImagePanelSelectedEventService panelSelectedService);

    void centerImage();
}
