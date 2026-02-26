package org.mydicomviewer.ui;

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

    public void validateInput() throws IllegalArgumentException, NumberFormatException {
        String centerText = center.getText();
        String widthText = width.getText();

        if (centerText == null || centerText.isEmpty()) {
            throw new IllegalArgumentException("Window center must be specified.");
        }
        if (widthText == null || widthText.isEmpty()) {
            throw new IllegalArgumentException("Window width must be specified.");
        }

        int widthValue = 0;

        try {
            Integer.parseInt(centerText);
            widthValue = Integer.parseInt(widthText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Window center and width must be integers.");
        }
        if (widthValue == 0) {
            throw new IllegalArgumentException("Window width cannot be equal to 0.");
        }

    }

    public int getWindowCenter() {
        return Integer.parseInt(center.getText());
    }

    public int getWindowWidth() {
        return Integer.parseInt(width.getText());
    }
}
