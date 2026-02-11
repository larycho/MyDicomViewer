package org.example.mydicomviewer.models;

import java.awt.image.BufferedImage;

public class DicomImage {

    private final BufferedImage image;

    public DicomImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() { return image; }

    public int getDepth() { return image.getColorModel().getPixelSize(); }

    public int getMaxPossibleValue() {
        int depth = getDepth();
        return 1 << depth;
    }

    public int getMinPossibleValue() {
        return 0;
    }
}
