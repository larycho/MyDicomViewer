package org.example.mydicomviewer.views.image.panel;

import org.example.mydicomviewer.services.ImagePanelSelectedEventService;

public interface ImagePanelToolbar {
    void setAxis(Axis axis);

    void addPanelSelectedService(ImagePanelSelectedEventService panelSelectedService);
}
