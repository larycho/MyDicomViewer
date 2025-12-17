package org.example.mydicomviewer.views.image.panel.bin;

import org.example.mydicomviewer.models.shapes.DrawableShape;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;
import org.example.mydicomviewer.views.image.DrawingTool;
import org.example.mydicomviewer.views.image.ImageTool;
import org.example.mydicomviewer.views.image.panel.*;
import org.example.mydicomviewer.views.image.panel.reslice.ImagePanelResliceToolbarImpl;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class ImagePanelResliceImpl extends JPanel implements ImagePanel {

    private ImagePanelWrapper imagePanelWrapper;

    private ImagePanelToolbar toolbar;
    private InnerImagePanel nestedImagePanel;

    public ImagePanelResliceImpl(ImagePanelWrapper wrapper) {
        this.imagePanelWrapper = wrapper;

        setLayout(new BorderLayout());
        addDefaultBorder();

        setupToolbar();
        setupNestedImagePanel();
        setDefaultTool();
    }

    public ImagePanelResliceImpl() {}

    public void setImagePanelWrapper(ImagePanelWrapper imagePanelWrapper) {
        this.imagePanelWrapper = imagePanelWrapper;

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
        //this.nestedImagePanel = new InnerImagePanelImpl(imagePanelWrapper);
        //add((Component) nestedImagePanel, BorderLayout.CENTER);
    }

    private void setupToolbar() {
        this.toolbar = new ImagePanelResliceToolbarImpl(imagePanelWrapper);
        add((Component) toolbar, BorderLayout.NORTH);
    }

    private void setDefaultTool() {
        //nestedImagePanel.setImageTool(new PanZoomTool(nestedImagePanel));
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

    @Override
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
    }

    @Override
    public void addPanelSelectedService(ImagePanelSelectedEventService panelSelectedService) {

    }

    @Override
    public void setPanZoomTool() {

    }

    @Override
    public void setWindowingTool() {

    }

    @Override
    public void setAxis(Axis axis) {

    }
}
