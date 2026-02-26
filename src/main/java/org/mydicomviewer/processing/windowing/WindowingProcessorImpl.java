package org.mydicomviewer.processing.windowing;

import java.awt.image.*;

public class WindowingProcessorImpl implements WindowingProcessor {

    private int windowLevel;
    private int windowWidth;
    private double rescaleSlope;
    private double rescaleIntercept;
    private boolean isSigned;
    private PhotometricInterpretation photometricInterpretation;

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
            return handleRGB(sourceImage);
        }

        int lutSize = getLutSize(sourceImage);

        ByteLookupTable lookupTable = generateLookUpTable(lutSize);

        return applyLookUpTable(sourceImage, lookupTable);
    }

    private int getLutSize(BufferedImage sourceImage) {
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
        byte[] lut = new byte[size];

        int min = windowLevel - (windowWidth / 2);
        int max = windowLevel + (windowWidth / 2);

        for (int i = 0; i < size; i++) {
            int trueValue = i;

            if (isSigned) {
                trueValue = (short) i;
            }

            int value = generateLookUpTableValue(trueValue, min, max);

            if (photometricInterpretation == PhotometricInterpretation.MONOCHROME1) {
                value = 255 - value;
            }
            lut[i] = (byte) value;
        }
        return new ByteLookupTable(0, lut);
    }

    private int generateLookUpTableValue(int i, int min, int max) {

        double hounsfieldUnits = (i * rescaleSlope) + rescaleIntercept;

        int value;

        if (hounsfieldUnits <= min) {
            value = 0;
        }
        else if (hounsfieldUnits >= max) {
            value = 255;
        }
        else {
            value = (int) (255.0 * (hounsfieldUnits - min) / (windowWidth == 0 ? 1 : windowWidth));
        }
        return value;
    }

    private BufferedImage handleRGB(BufferedImage sourceImage) {
        float alpha = (float) (windowWidth <= 0 ? 1.0 : 255.0 / windowWidth);
        float beta = - (alpha * (windowLevel - ((float) windowWidth / 2)));
        RescaleOp op = new RescaleOp(alpha, beta,null);
        return op.filter(sourceImage, null);
    }
}
