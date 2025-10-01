package org.example.mydicomviewer.views;

import org.example.mydicomviewer.display.DicomDisplayPanel;

import javax.swing.*;
import java.awt.*;

public class MainImagePanel extends JPanel {

    public MainImagePanel() {
        this.setLayout(new BorderLayout());
    }

    public void addImagePanel(DicomDisplayPanel panel) {
        clear();

        JComponent imagePanel = getImagePanel(panel);

        add(imagePanel, BorderLayout.CENTER);

        refresh();
    }

    private void refresh() {
        this.revalidate();
        this.repaint();
    }

    private void clear() {
        Component[] components = this.getComponents();
        for (Component component : components) {
            this.remove(component);
        }
    }

    private JComponent getImagePanel(DicomDisplayPanel panel) {
        JComponent imagePanel = panel.getPanel();
        // TODO
        imagePanel.setPreferredSize(new Dimension(1024, 1024));
        return imagePanel;
    }
}
