package org.example.mydicomviewer.processing.file;

import com.google.inject.Inject;
import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.util.TagUtils;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.models.DicomSeries;
import org.example.mydicomviewer.models.TagGroup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentedFileProcessorImpl implements FragmentedFileProcessor {

    private final FileImagesProcessor fileImagesProcessor;
    private final FileTagsProcessor fileTagsProcessor;

    @Inject
    public FragmentedFileProcessorImpl(FileImagesProcessor fileImagesProcessor, FileTagsProcessor fileTagsProcessor) {
        this.fileImagesProcessor = fileImagesProcessor;
        this.fileTagsProcessor = fileTagsProcessor;
    }

    @Override
    public DicomFile readFragmentedFile(List<File> partialFiles) {
        ArrayList<File> sortedFiles = sortFiles(partialFiles);
        ArrayList<DicomImage> dicomImages = new ArrayList<>();

        for (File file : sortedFiles) {
            DicomSeries series = fileImagesProcessor.getImageSeriesFromFile(file);
            dicomImages.addAll(series.getImages());
        }

        DicomSeries series = new DicomSeries(dicomImages);

        if (!sortedFiles.isEmpty()) {
            File file = sortedFiles.get(0);
            TagGroup tags = fileTagsProcessor.getTagsFromFile(file);
            return new DicomFile(sortedFiles.get(0), tags, series);
        }

        return null;
    }

    private ArrayList<File> sortFiles(List<File> partialFiles) {
        ArrayList<ListElement> elements = new ArrayList<>();
        for (File file : partialFiles) {
            try (DicomInputStream dicomInputStream = new DicomInputStream(file)) {
                Attributes attributes = dicomInputStream.readDataset();
                int[] tags = attributes.tags();
                for (int tag : tags) {
                    String address = TagUtils.toString(tag);
                    if (address.equals("(0020,0013)")) {
                        int index = Integer.parseInt(attributes.getString(tag));
                        ListElement element = new ListElement(index, file);
                        elements.add(element);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        elements.sort((o1, o2) -> o2.getIndex() - o1.getIndex());
        ArrayList<File> sortedFiles = new ArrayList<>();
        for (ListElement element : elements) {
            sortedFiles.add(element.getFile());
        }
        return sortedFiles;
    }

    private class ListElement {
        private final int index;
        private final File file;
        public ListElement(int index, File file) {
            this.index = index;
            this.file = file;
        }
        public int getIndex() {
            return index;
        }
        public File getFile() {
            return file;
        }
    }

}
