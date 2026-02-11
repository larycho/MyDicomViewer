package org.example.mydicomviewer.processing.image;

import java.awt.image.BufferedImage;

public interface WindowingProcessor {
    BufferedImage applyWindowing(BufferedImage image, WindowingParameters parameters);
}
