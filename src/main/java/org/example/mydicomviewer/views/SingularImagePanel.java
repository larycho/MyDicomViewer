package org.example.mydicomviewer.views;

import org.example.mydicomviewer.display.ImagePanelManager;
import org.example.mydicomviewer.views.image.ImageTool;
import org.example.mydicomviewer.views.image.PanZoomTool;
import org.example.mydicomviewer.views.image.WindowingTool;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class SingularImagePanel extends JPanel {

    private ImagePanelManager imageManager;
    private SingularImagePanelToolbar toolbar;
    private NestedImagePanel nestedImagePanel;

    public SingularImagePanel(ImagePanelManager imageManager) {
        this.imageManager = imageManager;
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEtchedBorder());
        setupToolbar();
        setupImageLabel();
        setDefaultTool();
        displayDefaultImage();
    }

    private void setupToolbar() {
        toolbar = new SingularImagePanelToolbar(this);
        add(toolbar, BorderLayout.NORTH);
    }

    private void setupImageLabel() {
        nestedImagePanel = new NestedImagePanel(imageManager);
        add(nestedImagePanel, BorderLayout.CENTER);
    }

    private void setDefaultTool() {
        setImageTool(new PanZoomTool(nestedImagePanel));
    }

    public void moveToNextFrame() {
        BufferedImage image = imageManager.moveToNextFrame();
        nestedImagePanel.setCurrentImage(image);
    }

    public void moveToPreviousFrame() {
        BufferedImage image = imageManager.moveToPreviousFrame();
        nestedImagePanel.setCurrentImage(image);
    }

    public void setWindowing(int level, int width) {
        imageManager.setWindowWidth(width);
        imageManager.setWindowLevel(level);

        BufferedImage image = imageManager.getFrameForDisplay();
        nestedImagePanel.setCurrentImage(image);
    }

    public void setImageTool(ImageTool imageTool) {
        nestedImagePanel.setImageTool(imageTool);
    }

    public void setPanZoomTool() {
        setImageTool(new PanZoomTool(nestedImagePanel));
    }

    public void setWindowingTool() {
        setImageTool(new WindowingTool(imageManager, nestedImagePanel));
    }

    public void refresh() {
        revalidate();
        repaint();
        nestedImagePanel.revalidate();
        nestedImagePanel.repaint();
    }

    public void switchOverlay() {
        nestedImagePanel.switchOverlay();
    }

    public void displayDefaultImage() {
        imageManager.setCurrentFrameNumber(0);
        BufferedImage currentImage = imageManager.getFrameForDisplay();
        nestedImagePanel.setCurrentImage(currentImage);
    }

    public void displayImage(BufferedImage currentImage) {
        nestedImagePanel.setCurrentImage(currentImage);
    }

}
