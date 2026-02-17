package org.example;

import org.mydicomviewer.models.DicomImage;
import org.mydicomviewer.models.DicomSeries;
import org.mydicomviewer.processing.io.file.FileImagesProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FileImagesProcessorImageIOImpl implements FileImagesProcessor {
    @Override
    public DicomSeries getImageSeriesFromFile(File file) {
        try {

            BufferedImage image = ImageIO.read(file);
            return getDicomSeries(image);

        } catch (IOException e) {
            return new DicomSeries();
        }
    }

    private DicomSeries getDicomSeries(BufferedImage image) {
        DicomSeries series = new DicomSeries();

        if (image != null) {
            DicomImage dicomImage = new DicomImage(image);
            series.add(dicomImage);
        }

        return series;
    }
}
