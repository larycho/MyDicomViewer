package org.example.mydicomviewer.views;

import org.example.mydicomviewer.listeners.ImageDisplayer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ToolBar extends JToolBar {

    private JButton previous;
    private JButton next;

    public ToolBar() {
        super();
        addFrameChangeArrows();
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
}
