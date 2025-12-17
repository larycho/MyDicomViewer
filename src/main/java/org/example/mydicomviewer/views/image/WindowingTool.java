package org.example.mydicomviewer.views.image;


import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;


import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class WindowingTool implements ImageTool {

    private Point click = new Point(0, 0);
    private ImagePanelWrapper wrapper;

    public WindowingTool(ImagePanelWrapper wrapper) {
        this.wrapper = wrapper;
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

        int level = wrapper.getWindowLevel();
        int width = wrapper.getWindowWidth();
        wrapper.setWindowing(level + delta.x, width + delta.y);

        click = currentPoint;
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }
}
