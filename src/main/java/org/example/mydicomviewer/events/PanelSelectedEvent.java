package org.example.mydicomviewer.events;

import org.example.mydicomviewer.views.SingularImagePanel;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

import java.util.EventObject;

public class PanelSelectedEvent extends EventObject {

    private final ImagePanelWrapper panel;

    public PanelSelectedEvent(Object source, ImagePanelWrapper panel) {
        super(source);
        this.panel = panel;
    }

    public ImagePanelWrapper getPanel() {
        return panel;
    }
}
