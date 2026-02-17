package org.mydicomviewer.processing.windowing;

import java.awt.image.BufferedImage;

public interface WindowingProcessor {
    BufferedImage applyWindowing(BufferedImage image, WindowingParameters parameters);
}
