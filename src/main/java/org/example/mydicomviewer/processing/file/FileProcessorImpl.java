package org.example.mydicomviewer.processing.file;

import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomSeries;
import org.example.mydicomviewer.models.TagGroup;
import java.io.File;


public class FileProcessorImpl implements FileProcessor {

    public DicomFile readFile(File file) {

        TagGroup tags = getTagsFromFile(file);
        DicomSeries series = getImageSeriesFromFile(file);

        return new DicomFile(file, tags, series);
    }

    private DicomSeries getImageSeriesFromFile(File file) {
        // TODO
        //FileImagesProcessor processor = new FileImagesProcessorImpl();
        //return processor.getImageSeriesFromFile(file);
        return new DicomSeries();
    }

    private TagGroup getTagsFromFile(File file) {
        FileTagsProcessor tagsProcessor = new FileTagsProcessorImpl();
        return tagsProcessor.getTagsFromFile(file);
    }

}
