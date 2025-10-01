package org.example.mydicomviewer.views;

import org.example.mydicomviewer.listeners.ImageDisplayer;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private MainImagePanel imagePanel;
    private MainMenuBar menuBar;

    public MainWindow() {
        setupMenuBar();
        setupMainImagePanel();
        setupListeners();

        setTitle("My Dicom Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private void setupListeners() {
        ImageDisplayer imageDisplayer = new ImageDisplayer(this.imagePanel);
        this.menuBar.addFileLoadedListener(imageDisplayer);
    }

    private void setupMainImagePanel() {
        this.imagePanel = new MainImagePanel();
        this.add(imagePanel);
    }

    private void setupMenuBar() {
        this.menuBar = new MainMenuBar();
        this.setJMenuBar(menuBar);
    }


}
