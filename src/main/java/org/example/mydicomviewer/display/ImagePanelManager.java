package org.example.mydicomviewer.display;

import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.models.shapes.DrawableShape;
import org.example.mydicomviewer.processing.image.WindowingProcessor;
import org.example.mydicomviewer.processing.image.WindowingProcessorImpl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImagePanelManager implements ImagePanelManagerInt {

    private final DicomFile dicomFile;

    private int windowLevel = 150;
    private int windowWidth = 300;
    private int currentFrame = 0;

    private boolean persistShapes = true;

    private List<DrawableShape> currentShapes = new ArrayList<>();
    private Map<Integer, List<DrawableShape>> shapesForEachFrame = new HashMap<>();

    public ImagePanelManager(DicomFile dicomFile) {
        this.dicomFile = dicomFile;
    }

    public OverlayText getOverlay() {
        OverlayGenerator overlayGenerator = new OverlayGeneratorImpl();
        return overlayGenerator.createOverlayText(dicomFile);
    }
    @Override
    public DicomFile getDicomFile() {
        return dicomFile;
    }

    public int getWindowLevel() {
        return windowLevel;
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowLevel(int windowLevel) {
        this.windowLevel = windowLevel;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public BufferedImage moveToNextFrame() {
        int maxIndex = dicomFile.getImages().size() - 1;
        if (currentFrame == maxIndex) {
            currentFrame = 0;
        }
        else {
            currentFrame++;
        }
        return getFrameForDisplay();
    }

    public BufferedImage moveToPreviousFrame() {
        int maxIndex = dicomFile.getImages().size() - 1;
        if (currentFrame == 0) {
            currentFrame = maxIndex;
        }
        else {
            currentFrame--;
        }
        return getFrameForDisplay();
    }

    public void setCurrentFrameNumber(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public void changeWindowLevelBy(int delta) {
        windowLevel = windowLevel + delta;
    }

    public void changeWindowWidthBy(int delta) {
        windowWidth = windowWidth + delta;
    }

    public BufferedImage getFrameForDisplay() {
        List<DicomImage> images = this.dicomFile.getImages();
        DicomImage currentImage = images.get(currentFrame);

        WindowingProcessor windowingProcessor = new WindowingProcessorImpl();
        return windowingProcessor.applyWindowing(currentImage.getImage(), windowLevel, windowWidth, 0, 1);
    }
    @Override
    public void setPersistShapesBetweenFrames(boolean persistShapes) {
        List<DrawableShape> shapes = new ArrayList<>(getCurrentShapes());
        clearShapes();

        this.persistShapes = persistShapes;
        addShapes(shapes);
    }

    @Override
    public void addShape(DrawableShape shape) {
        if (persistShapes) {
            currentShapes.add(shape);
        }
        else {
            List<DrawableShape> shapes = shapesForEachFrame.computeIfAbsent(currentFrame, k -> new ArrayList<>());
            shapes.add(shape);
        }
    }

    @Override
    public void addShapes(List<DrawableShape> shapes) {
        for (DrawableShape shape : shapes) {
            addShape(shape);
        }
    }

    @Override
    public void deleteShape(DrawableShape shape) {
        if (persistShapes) {
            currentShapes.remove(shape);
        }
        else {
            List<DrawableShape> shapes = shapesForEachFrame.get(currentFrame);
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

    @Override
    public List<DrawableShape> getCurrentShapes() {
        if (persistShapes) {
            return new ArrayList<>(currentShapes);
        }
        else {
            List<DrawableShape> shapes = shapesForEachFrame.get(currentFrame);
            if (shapes == null) {
                return new ArrayList<>();
            }
            return new ArrayList<>(shapes);
        }
    }

    @Override
    public boolean areShapesPersisted() {
        return persistShapes;
    }

    @Override
    public int getCurrentFrameNumber() {
        return currentFrame;
    }

    public Map<Integer, List<DrawableShape>> getAllShapes() {
        if (persistShapes) {
            Map<Integer, List<DrawableShape>> shapes = new HashMap<>();
            shapes.put(currentFrame, new ArrayList<>(currentShapes));
            return shapes;
        }
        else {
            return new HashMap<>(shapesForEachFrame);
        }
    }
}
