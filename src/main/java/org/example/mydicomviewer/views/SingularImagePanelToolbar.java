package org.example.mydicomviewer.views;

import javax.swing.*;

public class SingularImagePanelToolbar extends JToolBar {

    private JButton leftArrow;
    private JButton rightArrow;
    private JButton zoom;
    private JButton windowing;

    private JButton settings;
    private SingularImagePanel singularImagePanel;

    public SingularImagePanelToolbar(SingularImagePanel singularImagePanel) {
        super();
        setFloatable(false);
        this.singularImagePanel = singularImagePanel;

        addArrowsButtons();
        addZoomAndPanButton();
        addWindowingButton();
        addSettingsButton();
    }

    private void addArrowsButtons() {
        leftArrow = new JButton("Previous frame");
        rightArrow = new JButton("Next frame");

        leftArrow.addActionListener(e -> singularImagePanel.moveToPreviousFrame());
        rightArrow.addActionListener(e -> singularImagePanel.moveToNextFrame());

        add(leftArrow);
        add(rightArrow);
    }

    private void addZoomAndPanButton() {
        zoom = new JButton("Zoom & Pan");
        zoom.addActionListener(e -> singularImagePanel.setPanZoomTool());
        add(zoom);
    }

    private void addWindowingButton() {
        windowing = new JButton("Windowing");
        windowing.addActionListener(e -> singularImagePanel.setWindowingTool());
        add(windowing);
    }

    private void addSettingsButton() {
        settings = new JButton("Settings");
        add(Box.createGlue());

        settings.addActionListener(e -> {
            getPopupMenu().show(settings, 0, settings.getHeight());
        });

        add(settings);
    }

    private JPopupMenu getPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem overlayItem = new JMenuItem("Toggle overlay (on/off)");
        overlayItem.addActionListener(e -> {singularImagePanel.switchOverlay();});
        popupMenu.add(overlayItem);
        return popupMenu;
    }
}
