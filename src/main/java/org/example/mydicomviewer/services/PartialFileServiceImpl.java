package org.example.mydicomviewer.services;

import org.dcm4che3.data.Attributes;
import org.dcm4che3.io.DicomInputStream;
import org.dcm4che3.util.TagUtils;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.models.DicomSeries;
import org.example.mydicomviewer.models.TagGroup;
import org.example.mydicomviewer.processing.file.FileImagesProcessor;
import org.example.mydicomviewer.processing.file.FileTagsProcessor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PartialFileServiceImpl {

    private FileImagesProcessor fileImagesProcessor;
    private FileTagsProcessor fileTagsProcessor;

    public PartialFileServiceImpl(FileImagesProcessor fileImagesProcessor, FileTagsProcessor fileTagsProcessor) {
        this.fileImagesProcessor = fileImagesProcessor;
        this.fileTagsProcessor = fileTagsProcessor;
    }

    public DicomFile createMainDicomFile(ArrayList<File> partialFiles) {
        ArrayList<File> sortedFiles = sortFiles(partialFiles);
        ArrayList<DicomImage> dicomImages = new ArrayList<>();

        for (File file : sortedFiles) {
            DicomSeries series = fileImagesProcessor.getImageSeriesFromFile(file);
            dicomImages.addAll(series.getImages());
        }

        DicomSeries series = new DicomSeries(dicomImages);

        TagGroup tags = new TagGroup();

        if (!sortedFiles.isEmpty()) {
            File file = sortedFiles.get(0);
            tags = fileTagsProcessor.getTagsFromFile(file);
        }

        return new DicomFile(sortedFiles.get(0), tags, series);
    }

    private ArrayList<File> sortFiles(ArrayList<File> partialFiles) {
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
        private int index;
        private File file;
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
