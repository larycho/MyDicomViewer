package org.example.mydicomviewer.services;

import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.Tag;
import org.example.mydicomviewer.models.shapes.Point3D;
import org.example.mydicomviewer.views.image.panel.Axis;

import java.util.List;

public class DistanceCalculatorImpl implements DistanceCalculator {

    DicomFile file;

    @Override
    public void setFile(DicomFile file) {
        this.file = file;
    }

    @Override
    public double calculateDistance(Point3D p1, Point3D p2) {

        String pixelSpacing = getPixelSpacing();
        String sliceSpacing = getSliceSpacing();

        double verticalSpacing = getVerticalSpacing(pixelSpacing);
        double horizontalSpacing = getHorizontalSpacing(pixelSpacing);
        double sliceSpacingValue = getSliceSpacingValue(sliceSpacing);

        double deltaX = ( p1.getX() - p2.getX() ) * horizontalSpacing;
        double deltaY = ( p1.getY() - p2.getY() ) * verticalSpacing;
        double deltaZ = ( p1.getZ() - p2.getZ() ) * sliceSpacingValue;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
    }

    @Override
    public double calculateAspectRatio(Axis axis) {
        String pixelSpacing = getPixelSpacing();
        String sliceSpacing = getSliceSpacing();

        double verticalSpacing = getVerticalSpacing(pixelSpacing);
        double horizontalSpacing = getHorizontalSpacing(pixelSpacing);
        double sliceSpacingValue = getSliceSpacingValue(sliceSpacing);

        switch (axis) {
            case X -> {
                return verticalSpacing / sliceSpacingValue;
            }
            case Y -> {
                return sliceSpacingValue / horizontalSpacing;
            }
            case Z -> {
                return verticalSpacing / horizontalSpacing;
            }
            default -> {
                return 1.0;
            }
        }
    }

    private double getSliceSpacingValue(String sliceSpacing) {

        double defaultValue = 0.0;

        if (!sliceSpacing.isBlank()) {
            try {
                return Double.parseDouble(sliceSpacing);
            } catch (NumberFormatException nfe) {
                return defaultValue;
            }
        }

        return defaultValue;
    }

    private double getHorizontalSpacing(String pixelSpacing) {

        double defaultValue = 0.5;

        if (!pixelSpacing.isBlank()) {

            String[] result = pixelSpacing.split("\\\\");

            if (result.length == 2) {
                try {
                    return Double.parseDouble(result[1]);
                } catch (NumberFormatException nfe) {
                    return defaultValue;
                }
            }
        }

        return defaultValue;
    }

    private double getVerticalSpacing(String pixelSpacing) {

        double defaultValue = 0.5;

        if (!pixelSpacing.isBlank()) {

            String[] result = pixelSpacing.split("\\\\");

            if (result.length == 2) {
                try {
                    return Double.parseDouble(result[0]);
                } catch (NumberFormatException nfe) {
                    return defaultValue;
                }
            }
        }

        return defaultValue;
    }

    private String getPixelSpacing() {
        String spacing = "";

        Tag tag = getTagWithAddress("(0028,0030)");
        if (tag != null) {
            spacing = tag.getValue();
        }

        return spacing;
    }

    private String getSliceSpacing() {
        String spacing = "";

        Tag tag = getTagWithAddress("(0018,0088)");
        if (tag != null) {
            spacing = tag.getValue();
        }

        return spacing;
    }

    private Tag getTagWithAddress(String searchAddress) {
        List<Tag> tags = file.getTags().allTags();

        for (Tag tag : tags) {
            String address = tag.getAddress();
            if (address.contains(searchAddress)) {
                return tag;
            }
        }

        return null;
    }

}
