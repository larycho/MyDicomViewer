package org.example;

import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.models.DicomSeries;
import org.example.mydicomviewer.processing.file.FileImagesProcessor;

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
