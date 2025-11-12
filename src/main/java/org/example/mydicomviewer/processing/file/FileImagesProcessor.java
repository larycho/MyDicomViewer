package org.example.mydicomviewer.processing.file;

import org.example.mydicomviewer.models.DicomSeries;

import java.io.File;

public interface FileImagesProcessor {

    DicomSeries getImageSeriesFromFile(File file);
}
