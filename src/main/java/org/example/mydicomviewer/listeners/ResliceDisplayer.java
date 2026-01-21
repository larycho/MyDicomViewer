package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import org.example.mydicomviewer.display.SplitScreenElement;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.models.DicomImage;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;
import org.example.mydicomviewer.views.MultipleImagePanel;
import org.example.mydicomviewer.views.image.panel.Axis;
import org.example.mydicomviewer.views.image.panel.ImagePanelFactory;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResliceDisplayer implements FileLoadedListener {

    DicomFile dicomFile;
    private FileLoadEventService fileLoadEventService;
    private MultipleImagePanel multipleImagePanel;
    private ImagePanelSelectedEventService panelSelectedService;

    @Inject
    public ResliceDisplayer(FileLoadEventService fileLoadEventService,
                            MultipleImagePanel multipleImagePanel,
                            ImagePanelSelectedEventService panelSelectedService) {
        this.fileLoadEventService = fileLoadEventService;
        this.multipleImagePanel = multipleImagePanel;
        this.panelSelectedService = panelSelectedService;
        fileLoadEventService.addListener(this);
    }

    public ResliceDisplayer() {}

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        dicomFile = event.getFile();
    }

    public void display() {
        if (dicomFile == null) {
            return;
        }
        String path = dicomFile.getFilePath();
        List<DicomImage> images = dicomFile.getImages();

        int max = images.size();
        BufferedImage[] frames = new BufferedImage[max];

        for (int i = 0; i < max; i++) {
            DicomImage image = images.get(i);
            BufferedImage frame = image.getImage();
            frames[i] = frame;
        }
        ImagePanelWrapper reslicePanelX = ImagePanelFactory.createResliceImagePanel(dicomFile);
        ImagePanelWrapper reslicePanelY = ImagePanelFactory.createResliceImagePanel(dicomFile);
        ImagePanelWrapper reslicePanelZ = ImagePanelFactory.createResliceImagePanel(dicomFile);

        multipleImagePanel.clearDisplay();
        multipleImagePanel.setAndApplyMode(createScreenMode(Arrays.asList(new Point(0,0), new Point(1,0), new Point(2,0))));
        multipleImagePanel.addImage(reslicePanelZ);
        multipleImagePanel.addImage(reslicePanelY);
        multipleImagePanel.addImage(reslicePanelX);
// TODO
        reslicePanelX.addPanelSelectedService(panelSelectedService);
        reslicePanelY.addPanelSelectedService(panelSelectedService);
        reslicePanelZ.addPanelSelectedService(panelSelectedService);
        reslicePanelX.setAxis(Axis.X);
        reslicePanelY.setAxis(Axis.Y);
        reslicePanelZ.setAxis(Axis.Z);
//        reslicePanelX.setMultipleImagePanel(multipleImagePanel);
//        reslicePanelY.setMultipleImagePanel(multipleImagePanel);
//        reslicePanelZ.setMultipleImagePanel(multipleImagePanel);
    }

    private SplitScreenMode createScreenMode(List<Point> points) {
        ArrayList<SplitScreenElement> elements = new ArrayList<>();

        for (Point point : points) {
            SplitScreenElement element = new SplitScreenElement(point.x, point.y);
            elements.add(element);
        }

        return new SplitScreenMode(elements);
    }
}
