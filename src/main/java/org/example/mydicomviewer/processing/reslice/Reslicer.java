package org.example.mydicomviewer.processing.reslice;

import java.awt.*;
import java.awt.image.BufferedImage;

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

    public void setStack(BufferedImage[] stack) {
        this.stack = stack;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
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

    private BufferedImage getXAxisSlice(int index) {
        int imageType = BufferedImage.TYPE_BYTE_GRAY;
        BufferedImage slice = new BufferedImage(height, depth, imageType);
        for (int z = 0; z < depth; z++) {
            for (int y = 0; y < height; y++) {
                int rgb = stack[z].getRGB(index, y);
                slice.setRGB(y, z, rgb);
            }
        }
        return slice;
    }

    private BufferedImage getYAxisSlice(int index) {
        int imageType = BufferedImage.TYPE_BYTE_GRAY;
        BufferedImage slice = new BufferedImage(width, depth, imageType);

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
