package org.example.mydicomviewer.models;

import java.io.File;

public class DicomFile {

    private File file;
    private TagGroup tags;
    private DicomSeries series;

    public DicomFile(File file, TagGroup tags, DicomSeries series) {
        this.file = file;
        this.tags = tags;
        this.series = series;
    }
}
