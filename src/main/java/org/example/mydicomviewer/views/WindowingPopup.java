package org.example.mydicomviewer.views;

import javax.swing.*;
import java.awt.*;

public class WindowingPopup extends JPanel {

    JTextField center = new JTextField();
    JTextField width = new JTextField();

    public WindowingPopup() {
        super(new GridLayout(2, 2, 0, 5));
        constructPopup();
    }

    private void constructPopup() {
        add(new JLabel("Center:"));
        add(center);
        add(new JLabel("Width:"));
        add(width);
    }

    public int getWindowCenter() {
        return Integer.parseInt(center.getText());
    }

    public int getWindowWidth() {
        return Integer.parseInt(width.getText());
    }
}
