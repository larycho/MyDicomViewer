package org.example.mydicomviewer.views;

import com.google.inject.Inject;
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
    private Footer footer;

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
    }

    private void setupSubPanels() {
        add(multipleImagePanel, BorderLayout.CENTER);
        add(toolBar, BorderLayout.NORTH);
        add(menuBar, BorderLayout.SOUTH);
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

    public MainWindow() {
        setupMenuBar();
        setupDrawingPanel();
        setupTagPanel();
        setupFileListPanel();
        //setupMainImagePanel();
        setupMultipleImagePanel();
        setupToolbar();
        setupListeners();
        setupFooter();

        setTitle("My Dicom Viewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
    }

    private void setupFooter() {
        footer = new Footer();
        add(footer, BorderLayout.SOUTH);
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
    // TO BE DELETED
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
