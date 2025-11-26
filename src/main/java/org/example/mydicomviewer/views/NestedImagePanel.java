package org.example.mydicomviewer.views;

import org.example.mydicomviewer.display.ImagePanelManager;
import org.example.mydicomviewer.display.overlay.OverlayText;
import org.example.mydicomviewer.views.image.ImageTool;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;

public class NestedImagePanel extends JPanel {

    private BufferedImage currentImage;

    private double zoom = 1.0;
    private Point panOffset = new Point(0, 0);

    private ImageTool currentTool;
    private final ImagePanelManager imageManager;

    private JLabel topLeft;
    private JLabel topRight;
    private JLabel bottomLeft;
    private JLabel bottomRight;

    private OverlayText overlayText;
    private boolean overlayEnabled = true;

    public NestedImagePanel(ImagePanelManager imageManager) {
        this.imageManager = imageManager;
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setOverlay();
    }

    private void setOverlay() {
        overlayText = imageManager.getOverlay();
        addCornerLabels();
        updateBottomRight();
    }

    public void setCurrentImage(BufferedImage currentImage) {
        this.currentImage = currentImage;
        updateBottomRight();
        repaint();
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
        updateBottomRight();
        repaint();
    }

    public void switchOverlay() {
        overlayEnabled = !overlayEnabled;
        if (overlayEnabled) {
            setOverlay();
        }
        else {
            remove(topLeft);
            remove(topRight);
            remove(bottomLeft);
            remove(bottomRight);
            repaint();
        }
    }

    private void updateBottomRight() {
        if (!overlayEnabled) { return; }

        String bottomRightText = overlayText.getBottomRight();

        DecimalFormat df = new DecimalFormat("0.00");
        String zoomText = "Zoom: " + df.format(zoom);
        String windowLevelText = "Window Level: " + imageManager.getWindowLevel();
        String windowWidthText = "Window Width: " + imageManager.getWindowWidth();

        String combined = bottomRightText + "\n" + zoomText + "\n" + windowLevelText + "\n" + windowWidthText;
        bottomRight.setText(toHtml(combined));
    }

    public double getZoom() {
        return zoom;
    }

    public void setPan(Point p) {
        panOffset = new Point(p);
        repaint();
    }

    public Point getPan() {
        return new Point(panOffset);
    }

    public void setImageTool(ImageTool newTool) {
        if (currentTool != null) {
            removeMouseListener(currentTool);
            removeMouseMotionListener(currentTool);
            removeMouseWheelListener(currentTool);
        }

        currentTool = newTool;
        addMouseListener(currentTool);
        addMouseMotionListener(currentTool);
        addMouseWheelListener(currentTool);
    }

    private void addCornerLabels() {
        if (!overlayEnabled) { return; }

        addTopLeftText();
        addTopRightText();
        addBottomLeftText();
        addBottomRightText();
    }

    private void addTopLeftText() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        String topLeftText = toHtml(overlayText.getTopLeft());
        topLeft = new JLabel(topLeftText);
        topLeft.setForeground(Color.white);
        add(topLeft, c);
    }

    private void addTopRightText() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        String topRightText = toHtml(overlayText.getTopRight());
        topRight = new JLabel(topRightText);
        topRight.setForeground(Color.white);
        add(topRight, c);
    }

    private void addBottomLeftText() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LAST_LINE_START;
        String bottomLeftText = toHtml(overlayText.getBottomLeft());
        bottomLeft = new JLabel(bottomLeftText);
        bottomLeft.setForeground(Color.white);
        add(bottomLeft, c);
    }

    private void addBottomRightText() {
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 1.0;
        c.weighty = 1.0;

        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        String bottomRightText = toHtml(overlayText.getBottomRight());
        bottomRight = new JLabel(bottomRightText);
        bottomRight.setForeground(Color.white);
        add(bottomRight, c);
    }

    private String toHtml(String text) {
        String[] lines = text.split("\n");
        StringBuilder html = new StringBuilder();
        html.append("<html>");
        for (String line : lines) {
            html.append(line);
            html.append("<br>");
        }
        html.append("</html>");
        return html.toString();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        AffineTransform transform = new AffineTransform();
        transform.scale(zoom, zoom);
        transform.translate(panOffset.x, panOffset.y);

        g2.drawImage(currentImage, transform, null);
    }
}
