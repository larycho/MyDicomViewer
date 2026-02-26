package org.mydicomviewer.ui.image;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.events.PanelSelectedEvent;
import org.mydicomviewer.events.services.ImagePanelSelectedEventService;
import org.mydicomviewer.events.listeners.PanelSelectedListener;
import org.mydicomviewer.tools.DrawingTool;

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
    public boolean isAnyImageSelected() {
        return currentlySelected != null;
    }

    @Override
    public void panelSelected(PanelSelectedEvent event) {
        setSelectedImage(event.getPanel());
    }
}
