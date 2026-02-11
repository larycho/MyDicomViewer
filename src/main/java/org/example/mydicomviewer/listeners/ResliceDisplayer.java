package org.example.mydicomviewer.listeners;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.display.SplitScreenElement;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.events.FileLoadedEvent;
import org.example.mydicomviewer.events.RequestedResliceEvent;
import org.example.mydicomviewer.models.DicomFile;
import org.example.mydicomviewer.services.FileLoadEventService;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;
import org.example.mydicomviewer.services.ResliceEventService;
import org.example.mydicomviewer.views.MultipleImagePanel;
import org.example.mydicomviewer.views.image.panel.Axis;
import org.example.mydicomviewer.views.image.panel.ImagePanelFactory;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class ResliceDisplayer implements FileLoadedListener, ResliceEventListener {

    DicomFile dicomFile;
    private final MultipleImagePanel multipleImagePanel;
    private final ImagePanelSelectedEventService panelSelectedService;

    @Inject
    public ResliceDisplayer(FileLoadEventService fileLoadEventService,
                            ResliceEventService resliceEventService,
                            MultipleImagePanel multipleImagePanel,
                            ImagePanelSelectedEventService panelSelectedService) {
        this.multipleImagePanel = multipleImagePanel;
        this.panelSelectedService = panelSelectedService;
        fileLoadEventService.addListener(this);
        resliceEventService.addListener(this);
    }

    @Override
    public void fileLoaded(FileLoadedEvent event) {
        dicomFile = event.getFile();
    }

    @Override
    public void resliceRequested(RequestedResliceEvent event) {
        if (dicomFile == null) {
            return;
        }

        multipleImagePanel.clearDisplay();
        multipleImagePanel.setAndApplyMode(createScreenMode(Arrays.asList(new Point(0,0), new Point(1,0), new Point(2,0))));

        addNewReslicePanel(Axis.Z);
        addNewReslicePanel(Axis.Y);
        addNewReslicePanel(Axis.X);
    }

    private void addNewReslicePanel(Axis axis) {
        ImagePanelWrapper reslicePanel = ImagePanelFactory.createResliceImagePanel(dicomFile);
        multipleImagePanel.addImage(reslicePanel);
        reslicePanel.addPanelSelectedService(panelSelectedService);
        reslicePanel.setAxis(axis);
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
