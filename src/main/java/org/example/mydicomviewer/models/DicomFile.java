package org.example.mydicomviewer.models;

import com.pixelmed.display.SourceImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class DicomFile {

    private File file;
    private TagGroup tags;
    private DicomSeries series;

    public DicomFile(File file, TagGroup tags, DicomSeries series) {
        this.file = file;
        this.tags = tags;
        this.series = series;
    }

    public String getFilePath() {
        return file.getAbsolutePath();
    }

    public String getFileName() { return file.getName(); }

    public TagGroup getTags() { return tags; }

    public List<DicomImage> getImages() {
        return series.getImages();
    }

    public SourceImage getSourceImage() { return series.getSourceImage(); }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return file.getName();
    }
}
