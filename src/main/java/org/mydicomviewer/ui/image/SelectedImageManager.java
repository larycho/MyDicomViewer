package org.mydicomviewer.ui.image;

import org.mydicomviewer.tools.DrawingTool;

public interface SelectedImageManager {

    void setSelectedImage(ImagePanelWrapper image);

    ImagePanelWrapper getSelectedImage();

    void setDrawingTool(DrawingTool tool);
}
