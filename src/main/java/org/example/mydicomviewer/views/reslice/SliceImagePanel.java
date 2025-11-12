package org.example.mydicomviewer.views.reslice;


import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

class SliceImagePanel extends JPanel {
    private BufferedImage image;

    public void setImage(BufferedImage img) {
        this.image = img;
        if (img != null) {
            setPreferredSize(new Dimension(img.getWidth(), img.getHeight()));
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }
}
