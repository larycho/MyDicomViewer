package org.example.mydicomviewer.views;

import com.google.inject.Inject;
import org.example.mydicomviewer.display.SplitScreenElement;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.listeners.*;
import org.example.mydicomviewer.views.filelist.FileListPanel;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private MainMenuBar menuBar;
    private ToolBar toolBar;
    private DrawingOverlayPanel drawingPanel;
    private TagPanel tagPanel;
    private MultipleImagePanel multipleImagePanel;
    private FileListPanel fileListPanel;

    @Inject
    public MainWindow(MainMenuBar menuBar,
                      ToolBar toolBar,
                      TagPanel tagPanel,
                      MultipleImagePanel multipleImagePanel,
                      FileListPanel fileListPanel) {
        this.menuBar = menuBar;
        this.toolBar = toolBar;
        this.tagPanel = tagPanel;
        this.multipleImagePanel = multipleImagePanel;
        this.fileListPanel = fileListPanel;

        setupFrame();

        setupSubPanels();

        setupListeners();
    }

    private void setupSubPanels() {
        add(multipleImagePanel, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        add(tagPanel, BorderLayout.EAST);
        add(fileListPanel, BorderLayout.WEST);

        setJMenuBar(menuBar);
    }

    private void setupFrame() {
        setTitle("My Dicom Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
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


}
