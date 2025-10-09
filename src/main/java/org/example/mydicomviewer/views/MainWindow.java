package org.example.mydicomviewer.views;

import org.example.mydicomviewer.listeners.ImageDisplayer;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private MainImagePanel imagePanel;
    private MainMenuBar menuBar;
    private ToolBar toolBar;
    private DrawingOverlayPanel drawingPanel;

    public MainWindow() {
        setupMenuBar();
        setupDrawingPanel();
        setupMainImagePanel();
        setupToolbar();
        setupListeners();

        setTitle("My Dicom Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private void setupDrawingPanel() {
        drawingPanel = new DrawingOverlayPanel();
    }

    private void setupToolbar() {
        this.toolBar = new ToolBar();
        add(toolBar, BorderLayout.NORTH);
    }

    private void setupListeners() {
        ImageDisplayer imageDisplayer = new ImageDisplayer(this.imagePanel);
        this.menuBar.addFileLoadedListener(imageDisplayer);
        this.toolBar.addFrameChangeListeners(imageDisplayer);
        this.toolBar.addWindowingListeners(imageDisplayer);
        this.toolBar.setDrawingOverlay(drawingPanel);
        this.imagePanel.setDrawingPanel(drawingPanel);
    }

    private void setupMainImagePanel() {
        this.imagePanel = new MainImagePanel();
        imagePanel.showSelectFilePrompt();
        this.add(imagePanel);
    }

    private void setupMenuBar() {
        this.menuBar = new MainMenuBar();
        this.setJMenuBar(menuBar);
    }


}
