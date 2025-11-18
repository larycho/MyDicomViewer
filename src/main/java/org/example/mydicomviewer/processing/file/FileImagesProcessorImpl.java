package org.example.mydicomviewer.processing.file;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;
import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.models.DicomSeries;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FileImagesProcessorImpl implements FileImagesProcessor {

    @Override
    public DicomSeries getImageSeriesFromFile(File file) {
        SourceImage sourceImage = openFile(file);

        ArrayList<DicomImage> frames = extractFrames(sourceImage);

        DicomSeries series = new DicomSeries(frames);
        series.addSourceImage(sourceImage);
        return series;
    }

    private SourceImage openFile(File file) {
        try {
            return new SourceImage(file.getAbsolutePath());
        } catch (IOException | DicomException e) {
            throw new RuntimeException(e);
            // TODO
        }
    }

    private ArrayList<DicomImage> extractFrames(SourceImage sourceImage) {
        int max = sourceImage.getNumberOfFrames();
        ArrayList<DicomImage> frames = new ArrayList<>();

        for (int i = 0; i < max; i++) {
            BufferedImage bufferedImage = sourceImage.getBufferedImage(i);
            DicomImage frame = new DicomImage(bufferedImage);
            frames.add(frame);
        }
        return frames;
    }
}
