package org.example.mydicomviewer.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;
import java.util.ArrayList;

public class DrawingOverlayPanel extends JPanel {

    private boolean drawMode = false;
    private final List<List<Point>> drawingPaths = new ArrayList<>();
    private List<Point> currentPath = new ArrayList<>();
    private Color drawColor = Color.GREEN;
    private int brushSize = 5;

    public DrawingOverlayPanel() {
        setOpaque(false);
        setVisible(false);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (drawMode && SwingUtilities.isLeftMouseButton(e)) {

                    currentPath = new ArrayList<>();

                    currentPath.add(e.getPoint());
                    drawingPaths.add(currentPath);

                    repaint();
                    e.consume();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (drawMode && SwingUtilities.isLeftMouseButton(e)) {
                    currentPath = null;
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (drawMode && currentPath != null && SwingUtilities.isLeftMouseButton(e)) {
                    currentPath.add(e.getPoint());
                    repaint();
                    e.consume();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(drawColor);
        g2d.setStroke(new BasicStroke(brushSize, BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND));

        for (List<Point> path : drawingPaths) {
            if (path.size() > 1) {
                for (int i = 1; i < path.size(); i++) {
                    Point p1 = path.get(i - 1);
                    Point p2 = path.get(i);
                    g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
                }
            }
        }
    }

    public void setDrawMode(boolean drawMode) {
        this.drawMode = drawMode;
        setVisible(drawMode);
        setCursor(drawMode ? Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR) : Cursor.getDefaultCursor());
        repaint();
    }

    public void clearDrawing() {
        drawingPaths.clear();
        repaint();
    }

    @Override
    public boolean contains(int x, int y) {
        return drawMode;
    }
}
