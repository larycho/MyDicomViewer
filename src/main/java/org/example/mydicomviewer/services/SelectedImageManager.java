package org.example.mydicomviewer.services;

import org.example.mydicomviewer.views.image.DrawingTool;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

public interface SelectedImageManager {

    void setSelectedImage(ImagePanelWrapper image);

    void deselectImage();

    ImagePanelWrapper getSelectedImage();

    void setDrawingTool(DrawingTool tool);
}
