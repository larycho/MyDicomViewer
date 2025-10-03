package org.example.mydicomviewer.views;

import org.example.mydicomviewer.display.DicomDisplayPanel;

import javax.swing.*;
import java.awt.*;

public class MainImagePanel extends JPanel {

    private DicomDisplayPanel displayPanel;

    public MainImagePanel() {
        this.setLayout(new BorderLayout());
    }

    public void showSelectFilePrompt() {
        clear();

        JLabel prompt = createSelectFilePrompt();
        add(prompt);

        refresh();
    }

    private JLabel createSelectFilePrompt() {
        JLabel prompt = new JLabel("Select a file to display: File > Open file...");
        prompt.setHorizontalAlignment(SwingConstants.CENTER);
        return prompt;
    }

    public void addImagePanel(DicomDisplayPanel panel) {
        setDisplayPanel(panel);
        clear();

        JComponent imagePanel = panel.getPanel();
        add(imagePanel, BorderLayout.CENTER);
        this.setBackground(Color.BLACK);

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

    private void setDisplayPanel(DicomDisplayPanel panel) {
        this.displayPanel = panel;
    }

    public DicomDisplayPanel getDisplayPanel() { return displayPanel; }

    public boolean isDisplayPanelSet() { return displayPanel != null; }
}
