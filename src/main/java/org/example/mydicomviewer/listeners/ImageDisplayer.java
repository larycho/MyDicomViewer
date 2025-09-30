package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.display.DicomDisplayPanel;
import org.example.mydicomviewer.display.ImagePanelGenerator;
import org.example.mydicomviewer.display.ImagePanelGeneratorImpl;
import org.example.mydicomviewer.controllers.MainImagePanelController;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.models.DicomFile;

public class ImageDisplayer implements FileLoadedListener {

    private MainImagePanelController imagePanelController;

    public ImageDisplayer(MainImagePanelController imagePanelController) {
        this.imagePanelController = imagePanelController;
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        DicomFile dicomFile = event.getFile();

        ImagePanelGenerator generator = new ImagePanelGeneratorImpl();
        DicomDisplayPanel imageNode = generator.createImageNode(dicomFile);

        this.imagePanelController.addNodeToPane(imageNode);
    }
}
