package org.example.mydicomviewer.processing.image;

import org.example.mydicomviewer.processing.file.PhotometricInterpretation;

import java.awt.image.*;

public class WindowingProcessorImpl implements WindowingProcessor {

    private int windowLevel;
    private int windowWidth;
    private double rescaleSlope;
    private double rescaleIntercept;
    private boolean isSigned;
    private PhotometricInterpretation photometricInterpretation;

    @Override
    public BufferedImage applyWindowing(BufferedImage image, int windowLevel, int windowWidth,
                                        double rescaleIntercept, double rescaleSlope) {
        this.windowLevel = windowLevel;
        this.windowWidth = windowWidth;
        this.rescaleSlope = rescaleSlope;
        this.rescaleIntercept = rescaleIntercept;
        return applyWindowing(image);
    }

    @Override
    public BufferedImage applyWindowing(BufferedImage image, WindowingParameters parameters) {
        this.windowLevel = parameters.getWindowLevel();
        this.windowWidth = parameters.getWindowWidth();
        this.rescaleSlope = parameters.getRescaleSlope();
        this.rescaleIntercept = parameters.getRescaleIntercept();
        this.isSigned = parameters.isSigned();
        this.photometricInterpretation = parameters.getPhotometricInterpretation();
        return applyWindowing(image);
    }

    public BufferedImage applyWindowing(BufferedImage sourceImage) {
        if (photometricInterpretation == PhotometricInterpretation.RGB) {
            return sourceImage;
        }

        int lutSize = getLutSize(sourceImage);

        ByteLookupTable lookupTable = generateLookUpTable(lutSize);

        return applyLookUpTable(sourceImage, lookupTable);
    }

    private static int getLutSize(BufferedImage sourceImage) {
        int depth = sourceImage.getColorModel().getPixelSize();
        return 1 << depth;
    }

    private BufferedImage createBufferedImage(BufferedImage sourceImage) {
        ColorModel colorModel = sourceImage.getColorModel();

        WritableRaster raster = colorModel.createCompatibleWritableRaster(sourceImage.getWidth(), sourceImage.getHeight());
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();

        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    private BufferedImage applyLookUpTable(BufferedImage sourceImage, ByteLookupTable lookupTable) {
        BufferedImage newImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        LookupOp lookupOp = new LookupOp(lookupTable, null);
        try {
            lookupOp.filter(sourceImage, newImage);
        } catch (Exception e) {
            try {
                newImage = createBufferedImage(sourceImage);
                lookupOp.filter(newImage, newImage);
            }
            catch (Exception ex) {
                return sourceImage;
            }
            return sourceImage;
        }

        return newImage;
    }

    private ByteLookupTable generateLookUpTable(int size) {
        byte[] lut = new byte[size + 1];

        int min = windowLevel - (windowWidth / 2);
        int max = windowLevel + (windowWidth / 2);

        for (int i = 0; i < size; i++) {
            int trueValue = i;

            if (isSigned) {
                trueValue = (short) i;
            }

            int value = generateLookUpTableValue(trueValue, min, max);
            lut[i] = (byte) value;
        }
        return new ByteLookupTable(0, lut);
    }

    private int generateLookUpTableValue(int i, int min, int max) {

        double valueHu = (i * rescaleSlope) + rescaleIntercept;

        int value;

        if (valueHu <= min) {
            value = 0;
        }
        else if (valueHu >= max) {
            value = 255;
        }
        else {
            value = (int) (255.0 * (valueHu - min) / windowWidth);
        }
        return value;
    }
}
