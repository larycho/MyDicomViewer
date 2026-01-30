package org.example.mydicomviewer.views.image;

import org.example.mydicomviewer.models.shapes.StraightLine;
import org.example.mydicomviewer.views.image.panel.InnerImagePanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class LineTool implements DrawingTool {

    private InnerImagePanel innerImagePanel;
    private StraightLine line;

    public void setImagePanel(InnerImagePanel innerImagePanel) {
        this.innerImagePanel = innerImagePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (innerImagePanel == null) {return;}

        line = new StraightLine(Color.GREEN, 1.2f);
        Point2D.Double p = transformPoint(e.getX(), e.getY());
        line.start(p.getX(), p.getY());

        updateLabel();

        innerImagePanel.addShape(line);
        innerImagePanel.refresh();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (innerImagePanel == null || line == null) { return; }

        Point2D.Double p = transformPoint(e.getX(), e.getY());
        line.end(p.getX(), p.getY());

        updateLabel();

        innerImagePanel.refresh();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (innerImagePanel == null || line == null) {return;}

        Point2D.Double p = transformPoint(e.getX(), e.getY());
        line.setEndPoint(p.getX(), p.getY());

        updateLabel();

        innerImagePanel.refresh();
    }

    private void updateLabel() {

        Point2D.Double startPoint = line.getStartPoint();
        Point2D.Double endPoint = line.getEndPoint();
        double distance = innerImagePanel.getDistance(startPoint, endPoint);
        String label = String.format("%.2f mm", distance);
        line.setLabel(label);

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    private Point2D.Double transformPoint(double x, double y) {

        try {
            AffineTransform transform = innerImagePanel.getTransform();
            return (Point2D.Double) transform.inverseTransform(new Point2D.Double(x, y), null);
        } catch (NoninvertibleTransformException e) {
            return new Point2D.Double(x, y);
        }
    }

    @Override
    public String toString() {
        return "Straight line";
    }
}
