package org.example.mydicomviewer.views.image.panel;

import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.shapes.DrawableShape;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;
import org.example.mydicomviewer.views.image.DrawingTool;
import org.example.mydicomviewer.views.image.ImageTool;

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

    int getWindowLevel();

    int getWindowWidth();

    void setImageTool(ImageTool imageTool);

    void setDrawingTool(DrawingTool drawingTool);

    void setPanZoomTool();

    void setWindowingTool();

    void refresh();

    void toggleOverlay();

    OverlayText getOverlay();

    void select();

    void deselect();

    DicomFile getDicomFile();

    Map<Integer, List<DrawableShape>> getAllShapes();

    void setPersistShapes(boolean persistFrames);

    boolean areShapesPersisted();

    int getNumberOfFrames();

    void moveToFrame(int frameIndex);

    void setAxis(Axis axis);

    void displayDefaultImage();

    void addPanelSelectedService(ImagePanelSelectedEventService panelSelectedService);

}
