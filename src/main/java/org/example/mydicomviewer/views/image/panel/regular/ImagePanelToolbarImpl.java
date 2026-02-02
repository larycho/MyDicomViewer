package org.example.mydicomviewer.views.image.panel.regular;

import org.example.mydicomviewer.events.PanelSelectedEvent;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;

import org.example.mydicomviewer.views.image.panel.Axis;
import org.example.mydicomviewer.views.image.panel.ImagePanelToolbar;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

public class ImagePanelToolbarImpl extends JToolBar implements ImagePanelToolbar {

    private JButton select;
    private JButton settings;

    private final ImagePanelWrapper wrapper;
    private ImagePanelSelectedEventService selectedEventService;

    private final int DEFAULT_ICON_SIZE = 25;
    private final Color DEFAULT_ICON_COLOR = UIManager.getColor("Component.accentColor");

    public ImagePanelToolbarImpl(ImagePanelWrapper wrapper)
    {
        super();
        setFloatable(false);
        this.wrapper = wrapper;

        addArrowsButtons();
        addZoomAndPanButton();
        addWindowingButton();

        addSpacer();

        addSelectButton();
        addSettingsButton();
    }

    private void addSpacer() {
        add(Box.createGlue());
    }

    private void addSelectButton() {
        select = new JButton("Select");
        select.setSelected(false);
        select.addActionListener(e -> toggleSelect());
        add(select);
    }

    public void toggleSelect() {
        PanelSelectedEvent event = new PanelSelectedEvent(this, wrapper);
        selectedEventService.notifyListeners(event);
        showSelected();
    }

    private void addArrowsButtons() {
        FontIcon leftArrowIcon = FontIcon.of(MaterialDesignA.ARROW_LEFT_CIRCLE, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        JButton leftArrow = new JButton(leftArrowIcon);
        leftArrow.setToolTipText("Previous frame");

        FontIcon rightArrowIcon = FontIcon.of(MaterialDesignA.ARROW_RIGHT_CIRCLE, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        JButton rightArrow = new JButton(rightArrowIcon);
        rightArrow.setToolTipText("Next frame");

        leftArrow.addActionListener(e -> wrapper.moveToPreviousFrame());
        rightArrow.addActionListener(e -> wrapper.moveToNextFrame());

        add(leftArrow);
        add(rightArrow);
    }

    private void addZoomAndPanButton() {
        JButton zoom = new JButton("Zoom & Pan");
        zoom.addActionListener(e -> wrapper.setPanZoomTool());
        add(zoom);
    }

    private void addWindowingButton() {
        JButton windowing = new JButton("Windowing");
        windowing.addActionListener(e -> wrapper.setWindowingTool());
        add(windowing);
    }

    private void addSettingsButton() {
        settings = new JButton("Settings");

        settings.addActionListener(e -> getPopupMenu().show(settings, 0, settings.getHeight()));

        add(settings);
    }

    private JPopupMenu getPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();

        JMenuItem overlayItem = new JMenuItem("Toggle overlay (on/off)");
        overlayItem.addActionListener(e -> wrapper.toggleOverlay());
        popupMenu.add(overlayItem);

        JMenuItem centerItem = new JMenuItem("Center image");
        centerItem.addActionListener(e -> {
            wrapper.centerImage();
            wrapper.refresh();
        });
        popupMenu.add(centerItem);

        return popupMenu;
    }

    @Override
    public void setAxis(Axis axis) {

    }

    @Override
    public void addPanelSelectedService(ImagePanelSelectedEventService selectedEventService) {
        this.selectedEventService = selectedEventService;
    }

    @Override
    public void setFrameNumber(int frameIndex) {
    }

    @Override
    public void showSelected() {
        select.setText("Selected");
        select.setEnabled(false);
    }

    @Override
    public void showDeselected() {
        select.setText("Select");
        select.setEnabled(true);
    }
}
