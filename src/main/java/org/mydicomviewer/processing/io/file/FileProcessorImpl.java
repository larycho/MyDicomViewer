package org.mydicomviewer.processing.io.file;

import com.google.inject.Inject;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.models.DicomSeries;
import org.mydicomviewer.models.TagGroup;
import java.io.File;


public class FileProcessorImpl implements FileProcessor {

    private final FileImagesProcessor imagesProcessor;
    private final FileTagsProcessor tagsProcessor;

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
