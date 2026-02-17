package org.mydicomviewer.models;

import org.mydicomviewer.processing.tags.TagNumber;

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

    public boolean isDicomdir() {
        if (containsTag(TagNumber.MEDIA_STORAGE_SOP_CLASS_UID.getAddress())) {
            Tag tag = getTag(TagNumber.MEDIA_STORAGE_SOP_CLASS_UID.getAddress());
            return tag.getValue().contains("1.2.840.10008.1.3.10");
        }
        return false;
    }
}
