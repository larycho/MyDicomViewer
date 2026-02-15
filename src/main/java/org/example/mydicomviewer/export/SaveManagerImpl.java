package org.example.mydicomviewer.export;

import com.google.inject.Inject;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.processing.file.TagProcessor;
import org.example.mydicomviewer.processing.image.WindowingParameters;
import org.example.mydicomviewer.processing.image.WindowingProcessor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;


public class SaveManagerImpl implements SaveManager {

    private final WindowingProcessor windowingProcessor;

    @Inject
    public SaveManagerImpl(WindowingProcessor windowingProcessor) {
        this.windowingProcessor = windowingProcessor;
    }

    @Override
    public void save(SaveParams params) {
        params.validate();

        List<DicomFile> files = params.getFiles();
        for (DicomFile file : files) {
            saveFile(file, params);
        }
    }

    private void saveFile(DicomFile file, SaveParams params) {
        int imagesTotal = file.getImages().size();

        for (int i = 0; i < imagesTotal; i++) {
            saveImage(file, i, params);
        }
    }

    private void saveImage(DicomFile file, int index, SaveParams params) {

        File outputFile = getOutputFile(file, index, params);
        BufferedImage image = getBufferedImage(file, index);
        SaveFormat format = params.getFormat();

        saveImage(outputFile, image, format);
    }

    private BufferedImage getBufferedImage(DicomFile file, int index) {
        TagProcessor tagProcessor = new TagProcessor(file);
        WindowingParameters parameters = tagProcessor.getWindowingParameters();
        BufferedImage sourceImage = file.getImages().get(index).getImage();
        return windowingProcessor.applyWindowing(sourceImage, parameters);
    }

    private void saveImage(File outputFile, BufferedImage image, SaveFormat format) {
        String formatName = format.name().toLowerCase();
        try {
            ImageIO.write(image, formatName, outputFile);
        } catch (IOException e) {
            throw new SaveException("Failed to save image: " + outputFile.getAbsolutePath(), e);
        }

    }

    private File getOutputFile(DicomFile file, int index, SaveParams params) {
        String fileName = getFileName(file, index, params);
        File destinationDir = params.getTargetDirectory();

        File output = new File(destinationDir, fileName);
        return renameIfFileExists(output, params);
    }

    private File renameIfFileExists(File output, SaveParams params) {
        SaveFormat format = params.getFormat();
        File renamed = new File(output.getParent(), output.getName());
        int suffix = 1;
        while (renamed.exists()) {
            String newName = output.getName().replaceFirst("[.][^.]+$", "");
            renamed = new File(output.getParent(), newName + "(" + suffix + ")."  + format.name().toLowerCase());
            suffix++;
        }
        return renamed;
    }

    private String getFileName(DicomFile file, int index, SaveParams params) {
        SaveFormat format = params.getFormat();
        int totalImages = file.getImages().size();
        String baseName = file.getFileName().replaceFirst("[.][^.]+$", "");

        if (totalImages > 1) {
            return baseName + "_" + index + "." + format.name().toLowerCase();
        }
        else {
            return baseName + "." + format.name().toLowerCase();
        }
    }

}
