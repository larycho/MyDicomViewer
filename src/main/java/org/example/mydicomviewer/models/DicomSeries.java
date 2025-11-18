package org.example.mydicomviewer.models;

import com.pixelmed.display.SourceImage;

import java.util.ArrayList;
import java.util.List;

public class DicomSeries {

    private List<DicomImage> images;
    private SourceImage sourceImage;

    public DicomSeries() {
        this.images = new ArrayList<DicomImage>();
    }

    public DicomSeries(List<DicomImage> images) {
        this.images = images;
    }

    public void add(DicomImage image) {
        this.images.add(image);
    }

    public void addSourceImage(SourceImage sourceImage) {
        this.sourceImage = sourceImage;
    }

    public SourceImage getSourceImage() {
        return sourceImage;
    }

    public List<DicomImage> getImages() {
        return images;
    }
}
