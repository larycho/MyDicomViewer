package org.example.mydicomviewer.views.image.panel.regular;

import org.example.mydicomviewer.display.OverlayGenerator;
import org.example.mydicomviewer.display.OverlayGeneratorImpl;
import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.models.shapes.DrawableShape;
import org.example.mydicomviewer.models.shapes.Point3D;
import org.example.mydicomviewer.processing.file.TagProcessor;
import org.example.mydicomviewer.processing.image.WindowingParameters;
import org.example.mydicomviewer.processing.image.WindowingProcessor;
import org.example.mydicomviewer.processing.image.WindowingProcessorImpl;
import org.example.mydicomviewer.services.DistanceCalculator;
import org.example.mydicomviewer.services.DistanceCalculatorImpl;
import org.example.mydicomviewer.views.image.panel.Axis;
import org.example.mydicomviewer.views.image.panel.ImageManager;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.*;

public class ImageManagerImpl implements ImageManager {

    private final DicomFile dicomFile;

    private WindowingParameters windowingParameters;

    private int currentFrame = 0;

    private boolean persistShapes = true;

    private final List<DrawableShape> currentShapes = new ArrayList<>();
    private final Map<Integer, List<DrawableShape>> shapesForEachFrame = new HashMap<>();

    public ImageManagerImpl(DicomFile dicomFile) {
        this.dicomFile = dicomFile;
        initializeWindowingParams();
    }

    @Override
    public DicomFile getDicomFile() {
        return dicomFile;
    }

    @Override
    public BufferedImage getImageWithWindowing(int level, int width) {
        List<DicomImage> images = this.dicomFile.getImages();
        DicomImage currentImage = images.get(currentFrame);

        WindowingProcessor windowingProcessor = new WindowingProcessorImpl();
        return windowingProcessor.applyWindowing(currentImage.getImage(), windowingParameters);
    }

    @Override
    public int getWindowWidth() {
        return windowingParameters.getWindowWidth();
    }

    @Override
    public void setWindowWidth(int width) {
        windowingParameters.setWindowWidth(width);
    }

    @Override
    public int getWindowLevel() {
        return windowingParameters.getWindowLevel();
    }

    @Override
    public void setWindowLevel(int windowLevel) {
        windowingParameters.setWindowLevel(windowLevel);
    }

    @Override
    public void changeWindowLevelBy(int delta) {
        int newLevel = windowingParameters.getWindowLevel() + delta;
        windowingParameters.setWindowLevel(newLevel);
    }

    @Override
    public void changeWindowWidthBy(int delta) {
        int newWidth = windowingParameters.getWindowWidth() + delta;
        windowingParameters.setWindowWidth(newWidth);
    }

    @Override
    public void resetWindowing() {
        TagProcessor tagProcessor = new TagProcessor(dicomFile);
        windowingParameters = tagProcessor.getWindowingParameters();
    }

    @Override
    public BufferedImage moveToNextFrame() {
        int maxIndex = dicomFile.getImages().size() - 1;
        if (currentFrame == maxIndex) {
            currentFrame = 0;
        }
        else {
            currentFrame++;
        }
        return getCurrentFrame();
    }

    @Override
    public BufferedImage moveToPreviousFrame() {
        int maxIndex = dicomFile.getImages().size() - 1;
        if (currentFrame == 0) {
            currentFrame = maxIndex;
        }
        else {
            currentFrame--;
        }
        return getCurrentFrame();
    }

    @Override
    public void setCurrentFrameNumber(int frameNumber) {
        this.currentFrame = frameNumber;
    }

    @Override
    public int getCurrentFrameNumber() {
        return currentFrame;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        List<DicomImage> images = this.dicomFile.getImages();
        DicomImage currentImage = images.get(currentFrame);

        WindowingProcessor windowingProcessor = new WindowingProcessorImpl();
        return windowingProcessor.applyWindowing(currentImage.getImage(), windowingParameters);
    }

    @Override
    public void setPersistShapes(boolean persistShapes) {
        List<DrawableShape> shapes = new ArrayList<>(getShapesForCurrentFrame());
        clearShapes();

        this.persistShapes = persistShapes;
        addShapes(shapes);
    }

    @Override
    public boolean areShapesPersisted() {
        return persistShapes;
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
    public void removeShape(DrawableShape shape) {
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
    public List<DrawableShape> getShapesForCurrentFrame() {
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

    @Override
    public OverlayText getOverlay() {
        OverlayGenerator overlayGenerator = new OverlayGeneratorImpl();
        return overlayGenerator.createOverlayText(dicomFile);
    }

    @Override
    public int getNumberOfFrames() {
        return dicomFile.getImages().size();
    }

    @Override
    public void setAxis(Axis axis) {
    }

    @Override
    public double getDistance(Point2D.Double p1, Point2D.Double p2) {
        Point3D startPoint = get3DPoint(p1);
        Point3D endPoint = get3DPoint(p2);

        DistanceCalculator distanceCalculator = new DistanceCalculatorImpl();
        distanceCalculator.setFile(dicomFile);
        return distanceCalculator.calculateDistance(startPoint, endPoint);
    }

    @Override
    public double getAspectRatioShift() {
        DistanceCalculator distanceCalculator = new DistanceCalculatorImpl();
        distanceCalculator.setFile(dicomFile);
        return distanceCalculator.calculateAspectRatio(Axis.Z);
    }

    private Point3D get3DPoint(Point2D.Double p1) {
        return new Point3D(p1.getX(), p1.getY(), 0.0);
    }

    private void initializeWindowingParams() {
        TagProcessor tagProcessor = new TagProcessor(dicomFile);
        windowingParameters = tagProcessor.getWindowingParameters();
    }
}
