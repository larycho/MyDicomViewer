package org.mydicomviewer.ui.image;

import org.mydicomviewer.processing.overlay.OverlayText;
import org.mydicomviewer.processing.spatial.Axis;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.tools.shapes.DrawableShape;
import org.mydicomviewer.events.services.ImagePanelSelectedEventService;
import org.mydicomviewer.tools.DrawingTool;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

public class ImagePanelWrapperImpl implements ImagePanelWrapper {

    private final ImageManager imageManager;
    private ImagePanel imagePanel;

    public ImagePanelWrapperImpl(ImageManager imageManager) {
        this.imageManager = imageManager;
    }

    @Override
    public void setImagePanel(ImagePanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    @Override
    public void displayDefaultImage() {
        moveToFrame(0);
        toggleOverlay();
    }

    @Override
    public void addPanelSelectedService(ImagePanelSelectedEventService panelSelectedService) {
        imagePanel.addPanelSelectedService(panelSelectedService);
    }

    @Override
    public void centerImage() {
        imagePanel.centerImage();
    }

    @Override
    public int getCurrentFrameNumber() {
        return imageManager.getCurrentFrameNumber();
    }

    @Override
    public JComponent getPanel() {
        return (JComponent) imagePanel;
    }

    @Override
    public void moveToNextFrame() {
        BufferedImage image = imageManager.moveToNextFrame();
        List<DrawableShape> shapes = imageManager.getShapesForCurrentFrame();


        if (image != null) {
            imagePanel.setFrameNumber(imageManager.getCurrentFrameNumber());
            imagePanel.setImage(image);
            imagePanel.setShapes(shapes);
        }
    }

    @Override
    public void moveToPreviousFrame() {
        BufferedImage image = imageManager.moveToPreviousFrame();
        List<DrawableShape> shapes = imageManager.getShapesForCurrentFrame();

        if (image != null) {
            imagePanel.setFrameNumber(imageManager.getCurrentFrameNumber());
            imagePanel.setImage(image);
            imagePanel.setShapes(shapes);
        }
    }

    @Override
    public void setWindowing(int level, int width) {
        imageManager.setWindowLevel(level);
        imageManager.setWindowWidth(width);
        BufferedImage image = imageManager.getCurrentFrame();
        imagePanel.setImage(image);
    }

    @Override
    public void resetWindowing() {
        imageManager.resetWindowing();
        BufferedImage image = imageManager.getCurrentFrame();
        imagePanel.setImage(image);
    }

    @Override
    public int getWindowLevel() {
        return imageManager.getWindowLevel();
    }

    @Override
    public int getWindowWidth() {
        return imageManager.getWindowWidth();
    }

    @Override
    public void setDrawingTool(DrawingTool drawingTool) {
        imagePanel.setDrawingTool(drawingTool);
    }

    @Override
    public void setPanZoomTool() {
        imagePanel.setPanZoomTool();
    }

    @Override
    public void setWindowingTool() {
        imagePanel.setWindowingTool();
    }

    @Override
    public void refresh() {
        imagePanel.refresh();
    }

    @Override
    public void toggleOverlay() {
        imagePanel.toggleOverlay();
    }

    @Override
    public OverlayText getOverlay() {
        return imageManager.getOverlay();
    }

    @Override
    public void showSelected() {
        imagePanel.select();
    }

    @Override
    public void showDeselected() {
        imagePanel.deselect();
    }

    @Override
    public DicomFile getDicomFile() {
        return imageManager.getDicomFile();
    }

    @Override
    public Map<Integer, List<DrawableShape>> getAllShapes() {
        return imageManager.getAllShapes();
    }

    @Override
    public void switchPersistShapes() {
        imageManager.setPersistShapes(!imageManager.areShapesPersisted());
    }

    @Override
    public void setPersistShapes(boolean persistShapes) {
        imageManager.setPersistShapes(persistShapes);
    }

    @Override
    public boolean areShapesPersisted() {
        return imageManager.areShapesPersisted();
    }

    @Override
    public int getNumberOfFrames() {
        return imageManager.getNumberOfFrames();
    }

    @Override
    public void moveToFrame(int frameIndex) {
        imageManager.setCurrentFrameNumber(frameIndex);
        List<DrawableShape> shapes = imageManager.getShapesForCurrentFrame();

        BufferedImage image = imageManager.getCurrentFrame();
        imagePanel.setImage(image);
        imagePanel.setFrameNumber(frameIndex);
        imagePanel.setShapes(shapes);
    }

    @Override
    public void setAxis(Axis axis) {
        imageManager.setAxis(axis);
        BufferedImage image = imageManager.getCurrentFrame();
        imagePanel.setImage(image);
        imagePanel.centerImage();
        imagePanel.setAxis(axis);
    }

    @Override
    public Axis getAxis() {
        return imageManager.getAxis();
    }
}
