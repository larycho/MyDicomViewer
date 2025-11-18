package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.views.reslice.ReslicerWindow;

import java.awt.image.BufferedImage;
import java.util.List;

public class ResliceDisplayer implements FileLoadedListener {

    DicomFile dicomFile;
    private FileLoadEventService fileLoadEventService;

    @Inject
    public ResliceDisplayer(FileLoadEventService fileLoadEventService) {
        this.fileLoadEventService = fileLoadEventService;
        fileLoadEventService.addListener(this);
    }

    public ResliceDisplayer() {}

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        dicomFile = event.getFile();
    }

    public void display() {
        String path = dicomFile.getFilePath();
        List<DicomImage> images = dicomFile.getImages();

        int max = images.size();
        BufferedImage[] frames = new BufferedImage[max];

        for (int i = 0; i < max; i++) {
            DicomImage image = images.get(i);
            BufferedImage frame = image.getImage();
            frames[i] = frame;
        }

        ReslicerWindow reslicerWindow = new ReslicerWindow(frames);
        reslicerWindow.setVisible(true);
    }
}
