package org.example.mydicomviewer.models;

import java.util.ArrayList;
import java.util.List;

public class DicomSeries {

    private final List<DicomImage> images;

    public DicomSeries() {
        this.images = new ArrayList<>();
    }

    public DicomSeries(List<DicomImage> images) {
        this.images = images;
    }

    public void add(DicomImage image) {
        this.images.add(image);
    }

    public List<DicomImage> getImages() {
        return images;
    }
}
