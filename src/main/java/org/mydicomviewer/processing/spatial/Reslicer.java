package org.mydicomviewer.processing.spatial;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.models.DicomImage;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.List;

public class Reslicer {

    Cache<Integer, BufferedImage> cache = Caffeine.newBuilder()
            .maximumWeight(100_000_000)
            .weigher(((key, value) -> estimateWeight((BufferedImage) value)))
            .build();

    private BufferedImage[] stack;
    private int width;
    private int height;
    private int depth;
    private Axis axis = Axis.Z;

    public Reslicer(DicomFile file) {
        this.stack = getImageArray(file);
        this.height = stack[0].getHeight();
        this.width = stack[0].getWidth();
        this.depth = stack.length;
    }

    private BufferedImage[] getImageArray(DicomFile dicomFile) {
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

    public void setAxis(Axis axis) {
        this.axis = axis;
        cache.invalidateAll();
    }

    public BufferedImage getSlice(int index) {

        if (axis == Axis.Z) {
            return stack[index];
        }

        return cache.get(index, key -> {
            if (axis == Axis.Y) {
                return getYAxisSlice(index);
            }
            return getXAxisSlice(index);
        });
    }

    private BufferedImage createBufferedImage(int width, int height) {
        ColorModel colorModel = (stack.length != 0) ? stack[0].getColorModel() : ColorModel.getRGBdefault();

        WritableRaster raster = colorModel.createCompatibleWritableRaster(width, height);
        boolean isAlphaPremultiplied = colorModel.isAlphaPremultiplied();

        return new BufferedImage(colorModel, raster, isAlphaPremultiplied, null);
    }

    private BufferedImage getXAxisSlice(int index) {
        BufferedImage slice = createBufferedImage(depth, height);

        Object column = null;
        for (int z = 0; z < depth; z++) {
            WritableRaster raster = stack[z].getRaster();
            WritableRaster sliceRaster = slice.getRaster();

            column = raster.getDataElements(index, 0, 1, height, column);
            sliceRaster.setDataElements(z, 0, 1, height, column);
        }

        return slice;
    }

    private BufferedImage getYAxisSlice(int index) {
        BufferedImage slice = createBufferedImage(width, depth);
        Object row = null;
        for (int z = 0; z < depth; z++) {
            WritableRaster raster = stack[z].getRaster();
            WritableRaster sliceRaster = slice.getRaster();

            row = raster.getDataElements(0, index, width, 1, row);
            sliceRaster.setDataElements(0, z, width, 1, row);
        }

        return slice;
    }

    public int getMaxIndex(Axis axis) {
        return switch (axis) {
            case Z -> depth - 1;
            case Y -> height - 1;
            default -> // X
                    width - 1;
        };
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

    private int estimateWeight(BufferedImage value) {

        long pixelCount = (long) value.getWidth() * value.getHeight();
        int bytesPerPixel = value.getColorModel().getPixelSize() / 8;

        return (int) (pixelCount * bytesPerPixel);
    }
}
