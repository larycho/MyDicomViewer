package org.mydicomviewer.ui.image.reslice;

import org.mydicomviewer.processing.overlay.OverlayGenerator;
import org.mydicomviewer.processing.overlay.OverlayGeneratorImpl;
import org.mydicomviewer.ui.image.shapes.ShapeManager;
import org.mydicomviewer.ui.image.shapes.ShapeManagerImpl;
import org.mydicomviewer.processing.overlay.OverlayText;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.tools.shapes.DrawableShape;
import org.mydicomviewer.tools.shapes.Point3D;
import org.mydicomviewer.processing.tags.TagProcessor;
import org.mydicomviewer.processing.windowing.WindowingParameters;
import org.mydicomviewer.processing.windowing.WindowingProcessor;
import org.mydicomviewer.processing.windowing.WindowingProcessorImpl;
import org.mydicomviewer.processing.spatial.Reslicer;
import org.mydicomviewer.processing.spatial.DistanceCalculator;
import org.mydicomviewer.processing.spatial.DistanceCalculatorImpl;
import org.mydicomviewer.processing.spatial.Axis;
import org.mydicomviewer.ui.image.ImageManager;

import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImageManagerResliceImpl implements ImageManager {

    private final DicomFile dicomFile;

    private Axis currentAxis = Axis.Z;

    private WindowingParameters windowingParameters;

    private int currentFrame = 0;

    private Map<Axis, ShapeManager> shapeManagers;

    private final Reslicer reslicer;

    public ImageManagerResliceImpl(DicomFile dicomFile) {
        this.dicomFile = dicomFile;
        this.reslicer = new Reslicer(dicomFile);
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
        return windowingProcessor.applyWindowing(currentImage, windowingParameters);
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
        int maxFrameNumber = getMaxFrameIndex();

        if (frameNumber <= maxFrameNumber && frameNumber >= 0) {
            currentFrame = frameNumber;
        }
    }

    @Override
    public int getCurrentFrameNumber() {
        return currentFrame;
    }

    @Override
    public BufferedImage getCurrentFrame() {
        BufferedImage currentImage = getSlice(currentFrame);

        WindowingProcessor windowingProcessor = new WindowingProcessorImpl();
        return windowingProcessor.applyWindowing(currentImage, windowingParameters);
    }

    @Override
    public void setPersistShapes(boolean persistShapes) {
        ShapeManager manager = getShapeManager();
        manager.setPersisted(persistShapes, currentFrame);
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
    public Axis getAxis() {
        return currentAxis;
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
        windowingParameters = tagProcessor.getWindowingParameters();
    }


}
