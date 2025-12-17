package org.example.mydicomviewer.views.image.panel.reslice;

import org.example.mydicomviewer.events.PanelSelectedEvent;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;
import org.example.mydicomviewer.views.image.panel.Axis;
import org.example.mydicomviewer.views.image.panel.ImagePanelToolbar;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

import javax.swing.*;
import java.awt.*;

public class ImagePanelResliceToolbarImpl extends JToolBar implements ImagePanelToolbar {

    private JLabel sliceLabel;
    private JSlider sliceSlider;

    private JComboBox<String> axisSelector;
    private JButton settings;

    private ImagePanelWrapper wrapper;
    private ImagePanelSelectedEventService selectedEventService;

    private int depth;
    private boolean isSelected = false;

    public ImagePanelResliceToolbarImpl(ImagePanelWrapper wrapper) {
        this.wrapper = wrapper;
        this.depth = this.wrapper.getNumberOfFrames();

        createControlPanel(depth);
        add(Box.createGlue());
        addSettingsButtonAndMenu();
    }

    private void createControlPanel(int depth) {
        JPanel axisPanel = createAxisPanel();
        JPanel labelPanel = createSliceLabel(depth);
        JPanel sliderPanel = createSliceSliderPanel(depth);

        addListeners();

        add(axisPanel);
        add(labelPanel);
        add(sliderPanel);
    }

    private JPanel createSliceSliderPanel(int depth) {
        createSliceSlider(depth);
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(sliceSlider, BorderLayout.CENTER);
        return sliderPanel;
    }

    private void createSliceSlider(int depth) {
        sliceSlider = new JSlider(0, depth - 1, depth / 2);
        sliceSlider.setValue(0);

        if (depth - 1 > 1000) {
            sliceSlider.setMajorTickSpacing(500);
            sliceSlider.setMinorTickSpacing(250);
            sliceSlider.setLabelTable(sliceSlider.createStandardLabels(250));
        }
        else {
            sliceSlider.setMajorTickSpacing(100);
            sliceSlider.setMinorTickSpacing(25);
            sliceSlider.setLabelTable(sliceSlider.createStandardLabels(100));
        }
        sliceSlider.setPaintTicks(true);
        sliceSlider.setPaintLabels(true);
    }

    private JPanel createSliceLabel(int depth) {
        sliceLabel = new JLabel("Slice: " + (depth / 2));
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.add(sliceLabel);
        return labelPanel;
    }

    private JPanel createAxisPanel() {
        axisSelector = new JComboBox<>(new String[]{"Z Axis", "Y Axis", "X Axis"});
        JPanel axisPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        axisPanel.add(new JLabel("View Axis:"));
        axisPanel.add(axisSelector);
        return axisPanel;
    }

    private void addSettingsButtonAndMenu() {
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

        JMenuItem windowingMode = new JMenuItem("Windowing Mode");
        windowingMode.addActionListener(e -> wrapper.setWindowingTool());

        JMenuItem panAndZoomMode = new JMenuItem("Pan & Zoom Mode");
        panAndZoomMode.addActionListener(e -> wrapper.setPanZoomTool());

        JMenuItem selectImage = new JMenuItem("Select/Deselect image");
        selectImage.addActionListener(e -> {
            if (!isSelected) {
                PanelSelectedEvent event = new PanelSelectedEvent(this, wrapper);
                selectedEventService.notifyListeners(event);
                isSelected = true;
                wrapper.select();
            }
            else {
                isSelected = false;
                wrapper.deselect();
            }
        });

        popupMenu.add(overlayItem);
        popupMenu.add(windowingMode);
        popupMenu.add(panAndZoomMode);
        popupMenu.add(selectImage);

        return popupMenu;
    }

    private void addListeners() {
        axisSelector.addActionListener(e -> updateAxis());
        sliceSlider.addChangeListener(e -> updateView());
    }

    private void updateAxis() {
        Axis chosenAxis = getChosenAxis();

        wrapper.setAxis(chosenAxis);

        int maxIndex = wrapper.getNumberOfFrames() - 1;
        sliceSlider.setMaximum(maxIndex);
        adjustTickSpacing();
    }

    private Axis getChosenAxis() {
        int index = axisSelector.getSelectedIndex();

        if (index >= 0) {
            return switch (index) {
                case 1 -> Axis.Y;
                case 2 -> Axis.X;
                default -> Axis.Z;
            };
        }

        return Axis.Z;
    }

    @Override
    public void setAxis(Axis axis) {
        if (axis == Axis.Z) {
            axisSelector.setSelectedIndex(0);
        }
        if (axis == Axis.Y) {
            axisSelector.setSelectedIndex(1);
        }
        if (axis == Axis.X) {
            axisSelector.setSelectedIndex(2);
        }
        updateLabel();
    }

    private void updateView() {
        int sliceIndex = sliceSlider.getValue();

        int maxIndex = wrapper.getNumberOfFrames() - 1;

        if (sliceIndex > maxIndex) {
            sliceIndex = maxIndex;
            sliceSlider.setValue(sliceIndex);
        }

        sliceLabel.setText(String.format("Slice: %d / %d", sliceIndex, maxIndex));

        wrapper.moveToFrame(sliceIndex);
    }

    private void adjustTickSpacing() {
        int maxIndex = sliceSlider.getMaximum();

        if (maxIndex > 1000) {
            sliceSlider.setMajorTickSpacing(500);
            sliceSlider.setMinorTickSpacing(250);
            sliceSlider.setLabelTable(sliceSlider.createStandardLabels(250));
        }
        else {
            sliceSlider.setMajorTickSpacing(100);
            sliceSlider.setMinorTickSpacing(25);
            sliceSlider.setLabelTable(sliceSlider.createStandardLabels(100));
        }
    }

    @Override
    public void addPanelSelectedService(ImagePanelSelectedEventService panelSelectedService) {
        this.selectedEventService = panelSelectedService;
    }

    public void updateLabel() {
        int sliceIndex = sliceSlider.getValue();
        int maxIndex = wrapper.getNumberOfFrames() - 1;

        if (sliceSlider.getMaximum() != maxIndex) {
            sliceSlider.setMaximum(maxIndex);
        }

        if (sliceIndex > maxIndex) {
            sliceIndex = maxIndex;
            sliceSlider.setValue(sliceIndex);
        }

        sliceLabel.setText(String.format("Slice: %d / %d", sliceIndex, maxIndex));
        setMaxSliceLabelWidth(maxIndex);
    }

    private void setMaxSliceLabelWidth(int maxIndex) {
        String maxString = String.format("Slice: %d / %d", maxIndex, maxIndex);

        FontMetrics fontMetrics = sliceLabel.getFontMetrics(sliceLabel.getFont());
        int maxWidth = fontMetrics.stringWidth(maxString);

        sliceLabel.setPreferredSize(new Dimension(maxWidth + 10, sliceLabel.getPreferredSize().height));
    }
}
