package org.example;

import org.mydicomviewer.tools.DrawingTool;


import org.mydicomviewer.ui.image.InnerImagePanel;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

public class RectangleTool implements DrawingTool {

    private InnerImagePanel imagePanel;
    private Rectangle rectangle;

    public void setImagePanel(InnerImagePanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (imagePanel == null) {return;}

        rectangle = new Rectangle(Color.GREEN, 3.0f);
        Point2D.Double p = transformPoint(e.getX(), e.getY());
        rectangle.start(p.getX(), p.getY());

        imagePanel.addShape(rectangle);
        updateLabel();
        imagePanel.refresh();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (imagePanel == null || rectangle == null) {return;}

        Point2D.Double p = transformPoint(e.getX(), e.getY());
        rectangle.end(p.getX(), p.getY());

        updateLabel();
        imagePanel.refresh();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (imagePanel == null || rectangle == null) {return;}

        Point2D.Double p = transformPoint(e.getX(), e.getY());
        rectangle.setEndPoint(p.getX(), p.getY());

        updateLabel();
        imagePanel.refresh();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

    }

    private void updateLabel() {
        if (!imagePanel.isDistanceValid()) rectangle.setLabel(null);
        Point2D.Double startPoint = rectangle.getStartPoint();
        Point2D.Double endPoint = rectangle.getEndPoint();

        double perimeter = calculatePerimeter(startPoint, endPoint);
        double area = calculateArea(startPoint, endPoint);

        String label = String.format("Perimeter: %.2f mm, Area: %.2fmm2", perimeter, area);
        rectangle.setLabel(label);

    }

    private double calculatePerimeter(Point2D.Double startPoint, Point2D.Double endPoint) {
        Point2D.Double helper = new Point2D.Double(startPoint.getX(), endPoint.getY());

        double a = imagePanel.getDistance(startPoint, helper);
        double b = imagePanel.getDistance(endPoint, helper);

        return 2 * (a + b);
    }

    private double calculateArea(Point2D.Double startPoint, Point2D.Double endPoint) {
        Point2D.Double helper = new Point2D.Double(startPoint.getX(), endPoint.getY());

        double a = imagePanel.getDistance(startPoint, helper);
        double b = imagePanel.getDistance(endPoint, helper);

        return a * b;
    }

    private Point2D.Double transformPoint(double x, double y) {

        try {
            AffineTransform transform = imagePanel.getTransform();
            return (Point2D.Double) transform.inverseTransform(new Point2D.Double(x, y), null);
        } catch (NoninvertibleTransformException e) {
            return new Point2D.Double(x, y);
        }
    }

    @Override
    public String getToolName() {
        return "Rectangle";
    }

    @Override
    public String toString() {
        return getToolName();
    }

}
