package org.mydicomviewer.processing.io.file;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;
import org.mydicomviewer.models.DicomImage;
import org.mydicomviewer.models.DicomSeries;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

public class FileImagesProcessorImpl implements FileImagesProcessor {

    @Override
    public DicomSeries getImageSeriesFromFile(File file) {
        Optional<SourceImage> sourceImage = openFile(file);

        return sourceImage.map(this::imageReadCorrectly).orElseGet(this::imageCouldNotBeRead);
    }


    private DicomSeries imageReadCorrectly(SourceImage sourceImage) {
        ArrayList<DicomImage> frames = extractFrames(sourceImage);

        return new DicomSeries(frames);
    }

    private Optional<SourceImage> openFile(File file) {
        try {
            SourceImage sourceImage = new SourceImage(file.getAbsolutePath());
            return Optional.of(sourceImage);
        } catch (IOException | DicomException e) {
            return Optional.empty();
        }
    }

    private ArrayList<DicomImage> extractFrames(SourceImage sourceImage) {
        if (sourceImage == null) {
            return new ArrayList<>();
        }

        int max = sourceImage.getNumberOfFrames();
        ArrayList<DicomImage> frames = new ArrayList<>();

        for (int i = 0; i < max; i++) {
            BufferedImage bufferedImage = sourceImage.getBufferedImage(i);
            DicomImage frame = new DicomImage(bufferedImage);
            frames.add(frame);
        }
        return frames;
    }

    private DicomSeries imageCouldNotBeRead() {
        return new DicomSeries(new ArrayList<>());
    }
}
