package org.mydicomviewer.processing.io.file;

import org.mydicomviewer.models.DicomSeries;

import java.io.File;

public interface FileImagesProcessor {

    DicomSeries getImageSeriesFromFile(File file);
}
