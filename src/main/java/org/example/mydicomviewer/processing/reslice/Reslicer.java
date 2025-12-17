package org.example.mydicomviewer.processing.reslice;

import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.views.image.panel.Axis;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.List;

public class Reslicer {

    private int dimension;
    private BufferedImage[] stack;
    private int width;
    private int height;
    private int depth;
    private final int X = 0;
    private final int Y = 1;
    private final int Z = 2;


    public Reslicer(BufferedImage[] stack, int dimension) {
        this.dimension = dimension;
        this.stack = stack;
        this.height = stack[0].getHeight();
        this.width = stack[0].getWidth();
        this.depth = stack.length;
    }

    public Reslicer(DicomFile file, int dimension) {
        this.dimension = dimension;
        this.stack = getImageArray(file);
        this.height = stack[0].getHeight();
        this.width = stack[0].getWidth();
        this.depth = stack.length;
    }

    private BufferedImage[] getImageArray(DicomFile dicomFile) {
        String path = dicomFile.getFilePath();
        List<DicomImage> images = dicomFile.getImages();

        int max = images.size();
        BufferedImage[] frames = new BufferedImage[max];

        for (int i = 0; i < max; i++) {
            DicomImage image = images.get(i);
            BufferedImage frame = image.getImage();
            frames[i] = frame;
        }
        return frames;
    }

    public void setStack(BufferedImage[] stack) {
        this.stack = stack;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public void setAxis(Axis axis) {
        if (axis == Axis.X) {
            this.dimension = 0;
        }
        else if (axis == Axis.Y) {
            this.dimension = 1;
        }
        else {
            this.dimension = 2;
        }
    }

    public BufferedImage getSlice(int index) {

        int imageType = BufferedImage.TYPE_BYTE_GRAY;

        if (dimension == this.Z) {
            return stack[index];

        } else if (dimension == this.Y) {
            return getYAxisSlice(index);

        } else {
            return getXAxisSlice(index);
        }
    }

    private BufferedImage createBufferedImage(int width, int height) {
        ColorModel colorModel = (stack.length != 0) ? stack[0].getColorModel() : ColorModel.getRGBdefault();

        WritableRaster raster = colorModel.createCompatibleWritableRaster(width, height);
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();

        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    private BufferedImage getXAxisSlice(int index) {
        BufferedImage slice = createBufferedImage(depth, height);

        for (int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                int rgb = stack[z].getRGB(index, y);
                slice.setRGB(z, y, rgb);
            }
        }

        return slice;
    }

    private BufferedImage getYAxisSlice(int index) {
        BufferedImage slice = createBufferedImage(width, depth);

        for (int z = 0; z < depth; z++) {
            for (int x = 0; x < width; x++) {
                int rgb = stack[z].getRGB(x, index);
                slice.setRGB(x, z, rgb);
            }
        }

        return slice;
    }

    public int getMaxIndex(int dimension) {
        switch (dimension) {
            case Z:
                return depth - 1;
            case Y:
                return height - 1;
            default: // X
                return width - 1;
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDepth() {
        return depth;
    }
}
