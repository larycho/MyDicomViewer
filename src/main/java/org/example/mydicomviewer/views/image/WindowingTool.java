package org.example.mydicomviewer.views.image;

import org.example.mydicomviewer.display.ImagePanelManager;
import org.example.mydicomviewer.views.NestedImagePanel;
import org.example.mydicomviewer.views.SingularImagePanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

public class WindowingTool implements ImageTool {

    private int clickX, clickY;
    private Point click = new Point(0, 0);
    private final ImagePanelManager imageManager;
    private final NestedImagePanel nestedImagePanel;

    public WindowingTool(ImagePanelManager imageManager, NestedImagePanel nestedImagePanel) {
        this.imageManager = imageManager;
        this.nestedImagePanel = nestedImagePanel;
    }
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = new Point(e.getX(), e.getY());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point currentPoint = new Point(e.getX(), e.getY());
        Point delta = new Point(currentPoint.x - click.x, currentPoint.y - click.y);

        imageManager.changeWindowLevelBy(delta.x);
        imageManager.changeWindowWidthBy(delta.y);
        BufferedImage newImage = imageManager.getFrameForDisplay();

        nestedImagePanel.setCurrentImage(newImage);

        click = currentPoint;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
