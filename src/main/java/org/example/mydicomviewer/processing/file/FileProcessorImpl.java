package org.example.mydicomviewer.processing.file;

import com.google.inject.Inject;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomSeries;
import org.example.mydicomviewer.models.TagGroup;
import java.io.File;


public class FileProcessorImpl implements FileProcessor {

    private FileImagesProcessor imagesProcessor;
    private FileTagsProcessor tagsProcessor;

    @Inject
    public FileProcessorImpl(FileImagesProcessor imagesProcessor, FileTagsProcessor tagsProcessor) {
        this.imagesProcessor = imagesProcessor;
        this.tagsProcessor = tagsProcessor;
    }

    public DicomFile readFile(File file) {

        TagGroup tags = getTagsFromFile(file);
        DicomSeries series = getImageSeriesFromFile(file);

        return new DicomFile(file, tags, series);
    }

    private DicomSeries getImageSeriesFromFile(File file) {
        return imagesProcessor.getImageSeriesFromFile(file);
    }

    private TagGroup getTagsFromFile(File file) {
        return tagsProcessor.getTagsFromFile(file);
    }

}
