package org.example.mydicomviewer.views;

import org.example.mydicomviewer.listeners.ImageDisplayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JToolBar {

    private JButton previous;
    private JButton next;
    private JButton manualWindowing;

    public ToolBar() {
        super();
        addFrameChangeArrows();
        addWindowingButtons();
    }

    private void addWindowingButtons() {
        manualWindowing = new JButton("Manual Windowing");
        add(manualWindowing);
    }

    private void addFrameChangeArrows() {
        previous = new JButton("Previous frame");
        next = new JButton("Next frame");

        add(previous);
        add(next);
    }

    public void addFrameChangeListeners(ImageDisplayer imageDisplayer) {
        previous.addActionListener(e -> imageDisplayer.previousFrame());
        next.addActionListener(e -> imageDisplayer.nextFrame());
    }

    public void addWindowingListeners(ImageDisplayer imageDisplayer) {
        manualWindowing.addActionListener(e -> {
            WindowingPopup popup = new WindowingPopup();
            int result = JOptionPane.showConfirmDialog(null, popup, "Manual Windowing", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                changeWindowParameters(imageDisplayer, popup);
            }

        });
    }

    private void changeWindowParameters(ImageDisplayer imageDisplayer, WindowingPopup popup) {
        double center = popup.getWindowCenter();
        double width = popup.getWindowWidth();
        imageDisplayer.setWindowing(center, width);
    }


}
