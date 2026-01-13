package org.example.mydicomviewer.views.image.panel.reslice;

import org.example.mydicomviewer.display.OverlayGenerator;
import org.example.mydicomviewer.display.OverlayGeneratorImpl;
import org.example.mydicomviewer.display.ShapeManager;
import org.example.mydicomviewer.display.ShapeManagerImpl;
import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.shapes.DrawableShape;
import org.example.mydicomviewer.models.shapes.Point3D;
import org.example.mydicomviewer.processing.file.TagProcessor;
import org.example.mydicomviewer.processing.image.WindowingProcessor;
import org.example.mydicomviewer.processing.image.WindowingProcessorImpl;
import org.example.mydicomviewer.processing.reslice.Reslicer;
import org.example.mydicomviewer.services.DistanceCalculator;
import org.example.mydicomviewer.services.DistanceCalculatorImpl;
import org.example.mydicomviewer.views.image.panel.Axis;
import org.example.mydicomviewer.views.image.panel.ImageManager;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ImageManagerResliceImpl implements ImageManager {

    private DicomFile dicomFile;

    private Axis currentAxis = Axis.Z;
    private int windowLevel;
    private int windowWidth;
    private double rescaleIntercept;
    private double rescaleSlope;

    private int currentFrame = 0;

    private Map<Axis, ShapeManager> shapeManagers;

    private Reslicer reslicer;

    public ImageManagerResliceImpl(DicomFile dicomFile) {
        this.dicomFile = dicomFile;
        this.reslicer = new Reslicer(dicomFile, 2);
        initializeWindowingParams();
        createShapeManager();
    }

    private void createShapeManager() {
        shapeManagers = new HashMap<>();
        shapeManagers.put(Axis.X, new ShapeManagerImpl());
        shapeManagers.put(Axis.Y, new ShapeManagerImpl());
        shapeManagers.put(Axis.Z, new ShapeManagerImpl());
    }

    @Override
    public DicomFile getDicomFile() {
        return dicomFile;
    }

    @Override
    public BufferedImage getImageWithWindowing(int level, int width) {
        BufferedImage currentImage = getSlice(currentFrame);

        WindowingProcessor windowingProcessor = new WindowingProcessorImpl();
        return windowingProcessor.applyWindowing(currentImage, level, width, rescaleIntercept, rescaleSlope);

    }

    @Override
    public int getWindowWidth() {
        return windowWidth;
    }

    @Override
    public void setWindowWidth(int width) {
        this.windowWidth = width;
    }

    @Override
    public int getWindowLevel() {
        return windowLevel;
    }

    @Override
    public void setWindowLevel(int windowLevel) {
        this.windowLevel = windowLevel;
    }

    @Override
    public void changeWindowLevelBy(int delta) {
        windowLevel = windowLevel + delta;
    }

    @Override
    public void changeWindowWidthBy(int delta) {
        windowWidth = windowWidth + delta;
    }

    @Override
    public BufferedImage moveToNextFrame() {
        int maxFrameNumber = getMaxFrameIndex();

        if (currentFrame == maxFrameNumber) {
            currentFrame = 0;
        }
        else {
            currentFrame++;
        }

        return getCurrentFrame();
    }

    @Override
    public BufferedImage moveToPreviousFrame() {
        int maxFrameNumber = getMaxFrameIndex();

        if (currentFrame == 0) {
            currentFrame = maxFrameNumber;
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
        BufferedImage currentImage = getSlice(currentFrame);

        WindowingProcessor windowingProcessor = new WindowingProcessorImpl();
        return windowingProcessor.applyWindowing(currentImage, windowLevel, windowWidth, rescaleIntercept, rescaleSlope);
    }

    @Override
    public void setPersistShapes(boolean persistShapes) {
        ShapeManager manager = getShapeManager();
        manager.setPersisted(persistShapes);
    }

    @Override
    public boolean areShapesPersisted() {
        ShapeManager manager = getShapeManager();
        return manager.areShapesPersisted();
    }

    @Override
    public void addShape(DrawableShape shape) {
        ShapeManager manager = getShapeManager();
        manager.addShape(currentFrame, shape);
    }

    @Override
    public void addShapes(List<DrawableShape> shapes) {
        ShapeManager manager = getShapeManager();
        manager.addShapes(currentFrame, shapes);
    }

    @Override
    public void removeShape(DrawableShape shape) {
        ShapeManager manager = getShapeManager();
        manager.removeShape(currentFrame, shape);
    }

    @Override
    public void clearShapes() {
        ShapeManager manager = getShapeManager();
        manager.clearShapes();
    }

    @Override
    public List<DrawableShape> getShapesForCurrentFrame() {
        ShapeManager manager = getShapeManager();
        return manager.getShapesForFrame(currentFrame);
    }

    @Override
    public Map<Integer, List<DrawableShape>> getAllShapes() {
        ShapeManager manager = getShapeManager();
        return manager.getAllShapes();
    }

    private ShapeManager getShapeManager() {
        return shapeManagers.get(currentAxis);
    }

    @Override
    public OverlayText getOverlay() {
        OverlayGenerator overlayGenerator = new OverlayGeneratorImpl();
        return overlayGenerator.createOverlayText(dicomFile);
    }

    @Override
    public int getNumberOfFrames() {
        return getMaxFrameIndex() + 1;
    }

    @Override
    public void setAxis(Axis axis) {
        this.currentAxis = axis;
        updateReslicerAxis();
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
        return distanceCalculator.calculateAspectRatio(currentAxis);
    }

    private Point3D get3DPoint(Point2D.Double p1) {
        Point3D startPoint3D;
        if (currentAxis == Axis.X) {
            double x = 0.0;
            double y = p1.getY();
            double z = p1.getX();
            startPoint3D = new Point3D(x, y, z);
        }
        else if (currentAxis == Axis.Y) {
            double x = p1.getX();
            double y = 0.0;
            double z = p1.getY();
            startPoint3D = new Point3D(x, y, z);
        }
        else {
            startPoint3D = new Point3D(p1.getX(), p1.getY(), 0.0);
        }
        return startPoint3D;
    }

    private void updateReslicerAxis() {
        reslicer.setAxis(currentAxis);
    }

    private int getMaxFrameIndex() {
        int maxFrameNumber = 0;

        if (reslicer != null) {

            switch (currentAxis) {
                case Z:
                    maxFrameNumber = reslicer.getDepth() - 1;
                    break;
                case Y:
                    maxFrameNumber = reslicer.getHeight() - 1;
                    break;
                case X:
                    maxFrameNumber = reslicer.getWidth() - 1;
                    break;
                default:
                    break;
            }
        }

        return maxFrameNumber;
    }
    // could use reworking
    private BufferedImage getSlice(int sliceIndex) {
        int maxFrameNumber = getMaxFrameIndex();

        if (sliceIndex > maxFrameNumber) {
            sliceIndex = maxFrameNumber;
        }

        return reslicer.getSlice(sliceIndex);
    }

    private void initializeWindowingParams() {
        TagProcessor tagProcessor = new TagProcessor(dicomFile);

        Optional<Double> center = tagProcessor.getWindowCenter();
        Optional<Double> width = tagProcessor.getWindowWidth();
        Optional<Double> slope = tagProcessor.getRescaleSlope();
        Optional<Double> intercept = tagProcessor.getRescaleIntercept();

        windowLevel = (int) Math.round(center.orElse(150.0));
        windowWidth = (int) Math.round(width.orElse(300.0));
        rescaleIntercept = intercept.orElse(0.0);
        rescaleSlope = slope.orElse(1.0);
    }


}
