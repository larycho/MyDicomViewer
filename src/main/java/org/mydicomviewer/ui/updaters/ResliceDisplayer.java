package org.mydicomviewer.ui.updaters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.ui.display.SplitScreenElement;
import org.mydicomviewer.ui.display.SplitScreenMode;
import org.mydicomviewer.events.FileLoadedEvent;
import org.mydicomviewer.events.RequestedResliceEvent;
import org.mydicomviewer.events.listeners.FileLoadedListener;
import org.mydicomviewer.events.listeners.ResliceEventListener;
import org.mydicomviewer.models.DicomFile;
import org.mydicomviewer.events.services.FileLoadEventService;
import org.mydicomviewer.events.services.ImagePanelSelectedEventService;
import org.mydicomviewer.events.services.ResliceEventService;
import org.mydicomviewer.ui.MultipleImagePanel;
import org.mydicomviewer.processing.spatial.Axis;
import org.mydicomviewer.ui.image.ImagePanelFactory;
import org.mydicomviewer.ui.image.ImagePanelWrapper;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class ResliceDisplayer implements FileLoadedListener, ResliceEventListener {

    DicomFile dicomFile;
    private final MultipleImagePanel multipleImagePanel;
    private final ImagePanelSelectedEventService panelSelectedService;
    private final ImagePanelFactory imagePanelFactory;

    @Inject
    public ResliceDisplayer(FileLoadEventService fileLoadEventService,
                            ResliceEventService resliceEventService,
                            MultipleImagePanel multipleImagePanel,
                            ImagePanelSelectedEventService panelSelectedService,
                            ImagePanelFactory imagePanelFactory) {
        this.multipleImagePanel = multipleImagePanel;
        this.panelSelectedService = panelSelectedService;
        this.imagePanelFactory = imagePanelFactory;
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
        ImagePanelWrapper reslicePanel = imagePanelFactory.createResliceImagePanel(dicomFile);
        multipleImagePanel.addImage(reslicePanel);
        //reslicePanel.addPanelSelectedService(panelSelectedService);
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
