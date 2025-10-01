package org.example.mydicomviewer.display;

import com.pixelmed.display.SingleImagePanel;

import javax.swing.*;
import java.awt.*;

public class DicomDisplayPanelImpl extends JPanel implements DicomDisplayPanel {

    private SingleImagePanel imagePanel;

    public DicomDisplayPanelImpl(SingleImagePanel panel) {
        this.imagePanel = panel;
    }

    @Override
    public void refresh() {
        imagePanel.revalidate();
        imagePanel.repaint();
    }

    @Override
    public JComponent getPanel() {
        return imagePanel;
    }

    @Override
    public Dimension getPreferredSize() {
        imagePanel.getWidth();
        imagePanel.getHeight();

        return imagePanel.getPreferredSize();
    }
}
