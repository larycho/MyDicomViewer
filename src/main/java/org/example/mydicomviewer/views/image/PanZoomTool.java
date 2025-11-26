package org.example.mydicomviewer.views.image;

import org.example.mydicomviewer.views.NestedImagePanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class PanZoomTool implements ImageTool {

    private NestedImagePanel imagePanel;
    private Point click = new Point(0, 0);

    public PanZoomTool(NestedImagePanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        click = new Point(e.getX(), e.getY());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point currentPoint = new Point(e.getX(), e.getY());

        Point delta = calculateDelta(currentPoint);

        Point pan = imagePanel.getPan();
        Point newPan = new Point(pan.x + delta.x, pan.y + delta.y);

        imagePanel.setPan(newPan);
        imagePanel.repaint();
        
        click = currentPoint;
    }

    private Point calculateDelta(Point currentPoint) {
        int deltaX = currentPoint.x - click.x;
        int deltaY = currentPoint.y - click.y;
        return new Point(deltaX, deltaY);
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
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        double zoom = imagePanel.getZoom();

        if (e.getWheelRotation() < 0) {
            zoom *= 0.9;
        } else {
            zoom *= 1.1;
        }

        imagePanel.setZoom(zoom);
        imagePanel.repaint();
    }
}
