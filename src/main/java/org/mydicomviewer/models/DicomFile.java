package org.mydicomviewer.models;

import org.mydicomviewer.processing.tags.TagNumber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DicomFile {

    private final File file;
    private final TagSeries tags;
    private final DicomSeries series;

    public DicomFile(File file, TagSeries tags, DicomSeries series) {
        this.file = file;
        this.tags = tags;
        this.series = series;
    }

    public DicomFile(File file, TagGroup tags, DicomSeries series) {
        this.file = file;
        this.tags = new TagSeries(tags);
        this.series = series;
    }

    public String getFilePath() {
        return file.getAbsolutePath();
    }

    public String getFileName() { return file.getName(); }

    public TagGroup getTags() {
        return tags.getTags();
    }

    public TagGroup getTags(int index) {
        return tags.getTags(index);
    }

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

    public boolean containsTag(TagAddress address, int index) {
        return tags.containsTag(address, index);
    }

    public Tag getTag(TagAddress address) {
        return tags.getTag(address);
    }

    public Tag getTag(TagAddress address, int index) {
        return tags.getTag(address, index);
    }

    public boolean isDicomdir() {
        if (containsTag(TagNumber.MEDIA_STORAGE_SOP_CLASS_UID.getAddress())) {
            Tag tag = getTag(TagNumber.MEDIA_STORAGE_SOP_CLASS_UID.getAddress());
            return tag.getValue().contains("1.2.840.10008.1.3.10");
        }
        return false;
    }
}
