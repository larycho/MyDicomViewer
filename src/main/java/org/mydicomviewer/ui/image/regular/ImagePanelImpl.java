package org.mydicomviewer.ui.image.regular;

import org.mydicomviewer.processing.spatial.Axis;
import org.mydicomviewer.tools.shapes.DrawableShape;
import org.mydicomviewer.events.services.ImagePanelSelectedEventService;
import org.mydicomviewer.tools.DrawingTool;
import org.mydicomviewer.tools.ImageTool;
import org.mydicomviewer.tools.PanZoomTool;
import org.mydicomviewer.tools.WindowingTool;
import org.mydicomviewer.ui.image.*;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImagePanelImpl extends JPanel implements ImagePanel {

    private final ImagePanelWrapper imagePanelWrapper;
    private final ImageManager manager;

    private final ImagePanelToolbar toolbar;
    private InnerImagePanel nestedImagePanel;

    public ImagePanelImpl(ImageManager manager, ImagePanelWrapper wrapper, ImagePanelToolbar toolbar) {
        this.imagePanelWrapper = wrapper;
        this.manager = manager;
        this.toolbar = toolbar;

        setLayout(new BorderLayout());
        addDefaultBorder();
        setupToolbar();
        setupNestedImagePanel();
        setDefaultTool();
    }

    private void addDefaultBorder() {
        Border outside = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border inside = BorderFactory.createEtchedBorder();
        Border compound = BorderFactory.createCompoundBorder(outside, inside);
        setBorder(compound);
    }

    private void setupNestedImagePanel() {
        this.nestedImagePanel = new InnerImagePanelImpl(imagePanelWrapper, manager);
        add((Component) nestedImagePanel, BorderLayout.CENTER);
    }

    private void setupToolbar() {
        add((Component) toolbar, BorderLayout.NORTH);
    }

    private void setDefaultTool() {
        nestedImagePanel.setImageTool(new PanZoomTool(nestedImagePanel));
    }

    @Override
    public void setImage(BufferedImage image) {
        nestedImagePanel.setImage(image);
    }

    @Override
    public void setShapes(List<DrawableShape> shapes) {
        nestedImagePanel.setShapes(shapes);
    }

    @Override
    public void setImageTool(ImageTool imageTool) {
        nestedImagePanel.setImageTool(imageTool);
    }

    @Override
    public void setDrawingTool(DrawingTool drawingTool) {
        nestedImagePanel.setDrawingTool(drawingTool);
    }

    public void refresh() {
        revalidate();
        repaint();
    }

    @Override
    public void toggleOverlay() {
        nestedImagePanel.toggleOverlay();
    }

    @Override
    public void select() {
        addSelectionBorder();
        toolbar.showSelected();
        refresh();
    }

    private void addSelectionBorder() {
        Border compound = createSelectionBorder();
        setBorder(compound);
    }

    private static Border createSelectionBorder() {
        Color color = UIManager.getColor("Component.focusedBorderColor");

        int thickness = 5;
        boolean isRounded = true;

        Border outside = BorderFactory.createLineBorder(color, thickness, isRounded);
        Border inside = BorderFactory.createEtchedBorder();

        return BorderFactory.createCompoundBorder(outside, inside);
    }

    @Override
    public void deselect() {
        addDefaultBorder();
        toolbar.showDeselected();
        refresh();
    }

    @Override
    public void addPanelSelectedService(ImagePanelSelectedEventService panelSelectedService) {
        toolbar.addPanelSelectedService(panelSelectedService);
    }

    @Override
    public void setPanZoomTool() {
        nestedImagePanel.setImageTool(new PanZoomTool(nestedImagePanel));
    }

    @Override
    public void setWindowingTool() {
        nestedImagePanel.setImageTool(new WindowingTool(imagePanelWrapper));
    }

    @Override
    public void setAxis(Axis axis) {
        toolbar.setAxis(axis);
    }

    @Override
    public void centerImage() {
        nestedImagePanel.centerImage();
    }

    @Override
    public void setFrameNumber(int frameIndex) {
        toolbar.setFrameNumber(frameIndex);
    }
}
