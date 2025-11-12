package org.example.mydicomviewer.views;

import org.example.mydicomviewer.display.SplitScreenElement;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.listeners.*;
import org.example.mydicomviewer.views.filelist.FileListPanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private MainImagePanel imagePanel;
    private MainMenuBar menuBar;
    private ToolBar toolBar;
    private DrawingOverlayPanel drawingPanel;
    private TagPanel tagPanel;
    private MultipleImagePanel multipleImagePanel;
    private FileListPanel fileListPanel;

    public MainWindow() {
        setupMenuBar();
        setupDrawingPanel();
        setupTagPanel();
        setupFileListPanel();
        //setupMainImagePanel();
        setupMultipleImagePanel();
        setupToolbar();
        setupListeners();

        setTitle("My Dicom Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private void setupFileListPanel() {
        fileListPanel = new FileListPanel();
        add(fileListPanel, BorderLayout.WEST);
    }

    private void setupMultipleImagePanel() {
        SplitScreenMode mode = getDefaultMultiImageMode();
        multipleImagePanel = new MultipleImagePanel(mode);
        add(multipleImagePanel);
    }

    private SplitScreenMode getDefaultMultiImageMode() {
        SplitScreenElement element = new SplitScreenElement(0, 0);
        SplitScreenMode mode = new SplitScreenMode();
        mode.add(element);
        return mode;
    }

    private void setupDrawingPanel() {
        drawingPanel = new DrawingOverlayPanel();
    }

    private void setupToolbar() {
        this.toolBar = new ToolBar();
        add(toolBar, BorderLayout.NORTH);
    }

    private void setupTagPanel() {
        tagPanel = new TagPanel();
        add(tagPanel, BorderLayout.EAST);
    }

    private void setupListeners() {
        ImageDisplayer imageDisplayer = new ImageDisplayer(this.multipleImagePanel);
        ResliceDisplayer resliceDisplayer = new ResliceDisplayer();
        VolumeDisplayer volumeDisplayer = new VolumeDisplayer();
        FileListUpdater fileListUpdater = new FileListUpdater(fileListPanel);
        TagDisplayer tagDisplayer = new TagDisplayer(tagPanel);
        this.menuBar.addFileLoadedListener(imageDisplayer);
        this.menuBar.addFileLoadedListener(resliceDisplayer);
        this.menuBar.addFileLoadedListener(volumeDisplayer);
        this.menuBar.addFileLoadedListener(tagDisplayer);
        this.menuBar.addFileLoadedListener(fileListUpdater);
        this.toolBar.addFrameChangeListeners(imageDisplayer);
        this.toolBar.addWindowingListeners(imageDisplayer);
        this.toolBar.setDrawingOverlay(drawingPanel);
        //this.imagePanel.setDrawingPanel(drawingPanel);
        this.toolBar.addResliceListeners(resliceDisplayer);
        this.toolBar.add3DListeners(volumeDisplayer);
        this.toolBar.addScreenModeListeners(imageDisplayer);
        this.fileListPanel.addFileLoadedListener(fileListUpdater);
        this.fileListPanel.addFileLoadedListener(tagDisplayer);
        this.fileListPanel.addFileLoadedListener(imageDisplayer);
        this.fileListPanel.addFileLoadedListener(resliceDisplayer);
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
