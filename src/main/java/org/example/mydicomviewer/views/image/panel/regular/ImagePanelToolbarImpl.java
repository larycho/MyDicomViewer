package org.example.mydicomviewer.views.image.panel.regular;

import org.example.mydicomviewer.events.PanelSelectedEvent;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;
import org.example.mydicomviewer.views.MultipleImagePanel;
import org.example.mydicomviewer.views.SingularImagePanel;
import org.example.mydicomviewer.views.image.panel.Axis;
import org.example.mydicomviewer.views.image.panel.ImagePanelToolbar;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

import javax.swing.*;

public class ImagePanelToolbarImpl extends JToolBar implements ImagePanelToolbar {

    private JButton leftArrow;
    private JButton rightArrow;
    private JButton zoom;
    private JButton windowing;

    private JToggleButton select;
    private JButton settings;

    private ImagePanelWrapper wrapper;
    private ImagePanelSelectedEventService selectedEventService;

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
        select = new JToggleButton("Select");
        select.setSelected(false);
        select.addActionListener(e -> toggleSelect());
        add(select);
    }

    public void toggleSelect() {
        boolean hasJustBeenSelected = select.isSelected();

        if (hasJustBeenSelected) {
            PanelSelectedEvent event = new PanelSelectedEvent(this, wrapper);
            selectedEventService.notifyListeners(event);
            select.setText("Deselect");
            wrapper.select();
        }
        else {
            select.setText("Select");
            wrapper.deselect();
        }
    }

    private void addArrowsButtons() {
        leftArrow = new JButton("Previous frame");
        rightArrow = new JButton("Next frame");

        leftArrow.addActionListener(e -> wrapper.moveToPreviousFrame());
        rightArrow.addActionListener(e -> wrapper.moveToNextFrame());

        add(leftArrow);
        add(rightArrow);
    }

    private void addZoomAndPanButton() {
        zoom = new JButton("Zoom & Pan");
        zoom.addActionListener(e -> wrapper.setPanZoomTool());
        add(zoom);
    }

    private void addWindowingButton() {
        windowing = new JButton("Windowing");
        windowing.addActionListener(e -> wrapper.setWindowingTool());
        add(windowing);
    }

    private void addSettingsButton() {
        settings = new JButton("Settings");

        settings.addActionListener(e -> {
            getPopupMenu().show(settings, 0, settings.getHeight());
        });

        add(settings);
    }

    private JPopupMenu getPopupMenu() {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem overlayItem = new JMenuItem("Toggle overlay (on/off)");
        overlayItem.addActionListener(e -> {wrapper.toggleOverlay();});
        popupMenu.add(overlayItem);
        return popupMenu;
    }

    @Override
    public void setAxis(Axis axis) {

    }

    @Override
    public void addPanelSelectedService(ImagePanelSelectedEventService selectedEventService) {
        this.selectedEventService = selectedEventService;
    }
}
