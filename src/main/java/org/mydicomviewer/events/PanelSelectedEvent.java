package org.mydicomviewer.events;

import org.mydicomviewer.ui.image.ImagePanelWrapper;

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
