package org.example.mydicomviewer.display;

import javax.swing.*;
import java.awt.*;

public interface DicomDisplayPanel {

    void refresh();

    JComponent getPanel();

    Dimension getPreferredSize();

    void nextFrame();

    void previousFrame();

    void setWindowing(double center, double width);
}
