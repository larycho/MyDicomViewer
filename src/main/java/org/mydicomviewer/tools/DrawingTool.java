package org.mydicomviewer.tools;

import org.mydicomviewer.ui.image.InnerImagePanel;

public interface DrawingTool extends ImageTool {

    void setImagePanel(InnerImagePanel innerImagePanel);

    String getToolName();
}
