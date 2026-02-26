package org.mydicomviewer.ui.image;

import org.mydicomviewer.events.services.FrameSkipEventService;
import org.mydicomviewer.processing.spatial.Axis;
import org.mydicomviewer.events.services.ImagePanelSelectedEventService;

public interface ImagePanelToolbar {

    void setAxis(Axis axis);

    void addPanelSelectedService(ImagePanelSelectedEventService panelSelectedService);

    void addFrameSkipService(FrameSkipEventService frameSkipEventService);

    void setFrameNumber(int frameIndex);

    void showSelected();

    void showDeselected();
}
