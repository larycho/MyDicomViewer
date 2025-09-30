package org.example.mydicomviewer;

import com.formdev.flatlaf.FlatDarkLaf;
import org.example.mydicomviewer.views.MainWindow;

import javax.swing.*;

public class MyDicomViewer {

    public static void main(String[] args) {
        setupLookAndFeel();
        showMainWindow();
    }

    private static void showMainWindow() {
        MainWindow window = new MainWindow();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
    }

    private static void setupLookAndFeel() {
        FlatDarkLaf.setup();
    }
}
