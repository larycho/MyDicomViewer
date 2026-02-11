package org.example.mydicomviewer.models;

import java.io.File;
import java.util.List;

public class DicomFile {

    private final File file;
    private final TagGroup tags;
    private final DicomSeries series;

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

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return file.getName();
    }

    public boolean containsTag(TagAddress address) {
        return tags.containsTag(address);
    }

    public Tag getTag(TagAddress address) {
        return tags.getTag(address);
    }
}
