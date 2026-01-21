package org.example.mydicomviewer.processing.image;

import java.awt.image.BufferedImage;

public interface WindowingProcessor {
    public BufferedImage applyWindowing(BufferedImage image, int windowLevel, int windowWidth, double rescaleIntercept, double rescaleSlope);
    public BufferedImage applyWindowing(BufferedImage image, WindowingParameters parameters);
}
