package org.example.mydicomviewer.listeners;

import org.example.mydicomviewer.display.DicomDisplayPanel;
import org.example.mydicomviewer.display.ImagePanelGenerator;
import org.example.mydicomviewer.display.ImagePanelGeneratorImpl;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.views.MainImagePanel;

public class ImageDisplayer implements FileLoadedListener {

    private MainImagePanel imagePanel;

    public ImageDisplayer(MainImagePanel imagePanel) {
        this.imagePanel = imagePanel;
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        DicomFile dicomFile = event.getFile();

        ImagePanelGenerator generator = new ImagePanelGeneratorImpl();
        DicomDisplayPanel imageNode = generator.createImageNode(dicomFile);

        this.imagePanel.addImagePanel(imageNode);
    }
}
