package org.example.mydicomviewer.processing.image;

import java.awt.image.BufferedImage;
import java.awt.image.ByteLookupTable;
import java.awt.image.LookupOp;

public class WindowingProcessorImpl implements WindowingProcessor {

    private int windowLevel;
    private int windowWidth;

    @Override
    public BufferedImage applyWindowing(BufferedImage image, int windowLevel, int windowWidth) {
        this.windowLevel = windowLevel;
        this.windowWidth = windowWidth;
        return applyWindowing(image);
    }

    public BufferedImage applyWindowing(BufferedImage sourceImage) {
        int lutSize = getLutSize(sourceImage);

        ByteLookupTable lookupTable = generateLookUpTable(lutSize);

        return applyLookUpTable(sourceImage, lookupTable);
    }

    private static int getLutSize(BufferedImage sourceImage) {
        int depth = sourceImage.getColorModel().getPixelSize();
        return 1 << depth;
    }

    private BufferedImage applyLookUpTable(BufferedImage sourceImage, ByteLookupTable lookupTable) {
        BufferedImage newImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        LookupOp lookupOp = new LookupOp(lookupTable, null);
        lookupOp.filter(sourceImage, newImage);

        return newImage;
    }

    private ByteLookupTable generateLookUpTable(int size) {
        byte[] lut = new byte[size + 1];

        int min = windowLevel - (windowWidth / 2);
        int max = windowLevel + (windowWidth / 2);

        for (int i = 0; i < size; i++) {
            int value = generateLookUpTableValue(i, min, max);
            lut[i] = (byte) value;
        }
        return new ByteLookupTable(0, lut);
    }

    private int generateLookUpTableValue(int i, int min, int max) {
        int value;

        if (i <= min) {
            value = 0;
        }
        else if (i >= max) {
            value = 255;
        }
        else {
            value = (int) 255.0 * (i - min) / windowWidth;
        }
        return value;
    }
}
