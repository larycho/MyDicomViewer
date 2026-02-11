package org.example.mydicomviewer.services;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.events.PanelSelectedEvent;
import org.example.mydicomviewer.listeners.PanelSelectedListener;
import org.example.mydicomviewer.views.image.DrawingTool;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

@Singleton
public class SelectedImageManagerImpl implements SelectedImageManager, PanelSelectedListener {

    private ImagePanelWrapper currentlySelected;

    @Inject
    public SelectedImageManagerImpl(ImagePanelSelectedEventService eventService) {
        eventService.addListener(this);
    }

    @Override
    public void setSelectedImage(ImagePanelWrapper image) {
        if (currentlySelected != null) {
            currentlySelected.showDeselected();
        }
        currentlySelected = image;
        image.showSelected();
    }

    @Override
    public ImagePanelWrapper getSelectedImage() {
        return currentlySelected;
    }

    @Override
    public void setDrawingTool(DrawingTool tool) {
        if (currentlySelected != null) {
            currentlySelected.setDrawingTool(tool);
        }
    }

    @Override
    public void panelSelected(PanelSelectedEvent event) {
        setSelectedImage(event.getPanel());
    }
}
