package org.example.mydicomviewer.views.reslice;

import org.example.mydicomviewer.processing.reslice.Reslicer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ReslicerWindow extends JFrame {

    private Reslicer reslicer;
    private JSlider sliceSlider;
    private JComboBox<String> axisSelector;
    private SliceImagePanel imagePanel;
    private JLabel sliceLabel;

    public ReslicerWindow(BufferedImage[] stack) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        reslicer = new Reslicer(stack, 2);

        imagePanel = new SliceImagePanel();
        int depth = stack.length;

        JPanel controlPanel = createControlPanel(depth);

        add(imagePanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        addListeners();

        updateView();
        pack();
        setLocationRelativeTo(null);
    }

    private void addListeners() {
        axisSelector.addActionListener(e -> updateView());
        sliceSlider.addChangeListener(e -> updateView());
    }

    private void createSliceSlider(int depth) {
        sliceSlider = new JSlider(0, depth - 1, depth / 2);
        sliceSlider.setMajorTickSpacing(100);
        sliceSlider.setMinorTickSpacing(25);
        sliceSlider.setPaintTicks(true);
        sliceSlider.setPaintLabels(true);
    }

    private JPanel createControlPanel(int depth) {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));

        JPanel axisPanel = createAxisPanel();
        JPanel labelPanel = createSliceLabel(depth);
        JPanel sliderPanel = createSliceSliderPanel(depth);

        controlPanel.add(axisPanel);
        controlPanel.add(labelPanel);
        controlPanel.add(sliderPanel);

        return controlPanel;
    }

    private JPanel createSliceSliderPanel(int depth) {
        createSliceSlider(depth);
        JPanel sliderPanel = new JPanel(new BorderLayout());
        sliderPanel.add(sliceSlider, BorderLayout.CENTER);
        return sliderPanel;
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

    private void updateView() {
        String selectedAxis = (String) axisSelector.getSelectedItem();
        int sliceIndex = sliceSlider.getValue();

        int maxIndex = 0;

        if ("Z Axis".equals(selectedAxis)) {
            maxIndex = reslicer.getDepth() - 1;
            reslicer.setDimension(2);
        } else if ("Y Axis".equals(selectedAxis)) {
            maxIndex = reslicer.getHeight() - 1;
            reslicer.setDimension(1);
        } else if ("X Axis".equals(selectedAxis)) {
            maxIndex = reslicer.getWidth() - 1;
            reslicer.setDimension(0);
        }

        if (sliceSlider.getMaximum() != maxIndex) {
            sliceSlider.setMaximum(maxIndex);
        }

        if (sliceIndex > maxIndex) {
            sliceIndex = maxIndex;
            sliceSlider.setValue(sliceIndex);
        }

        sliceLabel.setText(String.format("Slice: %d / %d", sliceIndex, maxIndex));

        BufferedImage sliceImage = reslicer.getSlice(sliceIndex);
        imagePanel.setImage(sliceImage);
        pack();
    }

}
