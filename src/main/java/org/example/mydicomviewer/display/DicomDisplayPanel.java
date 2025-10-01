package org.example.mydicomviewer.display;

import javax.swing.*;
import java.awt.*;

public interface DicomDisplayPanel {

    void refresh();

    JComponent getPanel();

    Dimension getPreferredSize();
}
