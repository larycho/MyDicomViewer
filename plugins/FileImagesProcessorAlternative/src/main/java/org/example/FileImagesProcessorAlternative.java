package org.example;

import com.google.inject.Inject;
import org.example.mydicomviewer.models.DicomSeries;
import org.example.mydicomviewer.processing.file.FileImagesProcessor;
import org.example.mydicomviewer.processing.file.FileImagesProcessorImpl;

import java.io.File;

public class FileImagesProcessorAlternative implements FileImagesProcessor {
    private FileImagesProcessor imageIO, defaultProcessor;

    @Inject
    public FileImagesProcessorAlternative(FileImagesProcessorImageIOImpl imageIO,
                                          FileImagesProcessorImpl defaultProcessor)
    {
        this.imageIO = imageIO;
        this.defaultProcessor = defaultProcessor;
    }

    @Override
    public DicomSeries getImageSeriesFromFile(File file) {

        DicomSeries series = defaultProcessor.getImageSeriesFromFile(file);

        if (series == null || series.getImages().isEmpty()) {
            series = imageIO.getImageSeriesFromFile(file);
        }

        return series;
    }
}
