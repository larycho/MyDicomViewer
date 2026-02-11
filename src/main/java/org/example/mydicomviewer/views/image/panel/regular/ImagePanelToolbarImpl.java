package org.example.mydicomviewer.views.image.panel.regular;

import org.example.mydicomviewer.events.PanelSelectedEvent;
import org.example.mydicomviewer.services.ImagePanelSelectedEventService;

import org.example.mydicomviewer.views.image.panel.Axis;
import org.example.mydicomviewer.views.image.panel.ImagePanelToolbar;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.materialdesign2.MaterialDesignC;
import org.kordamp.ikonli.materialdesign2.MaterialDesignS;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;

public class ImagePanelToolbarImpl extends JToolBar implements ImagePanelToolbar {

    private JButton selectButton;
    private JButton settings;
    private JSlider slider;
    private JLabel frameInfo;

    private final ImagePanelWrapper wrapper;
    private ImagePanelSelectedEventService selectedEventService;

    private final int DEFAULT_ICON_SIZE = 25;
    private final Color DEFAULT_ICON_COLOR = UIManager.getColor("Component.accentColor");
    private final Color DISABLED_ICON_COLOR = UIManager.getColor("Button.disabledText");

    private int numberOfFrames;

    public ImagePanelToolbarImpl(ImagePanelWrapper wrapper)
    {
        super();
        setFloatable(false);
        this.wrapper = wrapper;
        numberOfFrames = wrapper.getNumberOfFrames();

        addArrowsButtons();
        addZoomAndPanButton();
        addWindowingButton();

        if (numberOfFrames > 1) {
            addFrameInfoLabel();
            addSlider();
        }
        else {
            addSpacer();
        }

        addSelectButton();
        addSettingsButton();
    }

    private void addFrameInfoLabel() {
        frameInfo = new JLabel("Frame: 0/" + (numberOfFrames - 1));
        setPreferredLabelWidth();
        add(frameInfo);
    }

    private void setPreferredLabelWidth() {
        String maxString = String.format("Slice: %d / %d", numberOfFrames, numberOfFrames);

        FontMetrics fontMetrics = frameInfo.getFontMetrics(frameInfo.getFont());
        int maxWidth = fontMetrics.stringWidth(maxString);

        frameInfo.setPreferredSize(new Dimension(maxWidth + 5, frameInfo.getPreferredSize().height));
        frameInfo.setMinimumSize(new Dimension(maxWidth + 5, frameInfo.getPreferredSize().height));
    }

    private void addSlider() {
        createSlider();
        slider.addChangeListener(e -> {
            wrapper.moveToFrame(slider.getValue());
        });
        add(slider);
    }

    private void createSlider() {
        slider = new JSlider(0, numberOfFrames - 1);
        slider.setValue(0);
        adjustTickSpacing();
    }

    private void adjustTickSpacing() {
        if (numberOfFrames < 10) {
            slider.setMajorTickSpacing(1);
        }
        else {
            slider.setMajorTickSpacing(numberOfFrames / 10);
        }

        slider.setPaintTicks(true);
    }

    private void addSpacer() {
        add(Box.createGlue());
    }

    private void addSelectButton() {
        FontIcon selectIcon = FontIcon.of(MaterialDesignS.SELECT_ALL, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        selectButton = new JButton(selectIcon);
        selectButton.setToolTipText("Select this image");
        selectButton.setSelected(false);
        selectButton.addActionListener(e -> toggleSelect());
        add(selectButton);
    }

    public void toggleSelect() {
        PanelSelectedEvent event = new PanelSelectedEvent(this, wrapper);
        selectedEventService.notifyListeners(event);
        showSelected();
    }

    private void addArrowsButtons() {
        Color buttonColor = DEFAULT_ICON_COLOR;
        if (numberOfFrames <= 1) {
            buttonColor = DISABLED_ICON_COLOR;
        }

        FontIcon leftArrowIcon = FontIcon.of(MaterialDesignA.ARROW_LEFT_CIRCLE, DEFAULT_ICON_SIZE, buttonColor);
        JButton leftArrow = new JButton(leftArrowIcon);
        leftArrow.setToolTipText("Previous frame");

        FontIcon rightArrowIcon = FontIcon.of(MaterialDesignA.ARROW_RIGHT_CIRCLE, DEFAULT_ICON_SIZE, buttonColor);
        JButton rightArrow = new JButton(rightArrowIcon);
        rightArrow.setToolTipText("Next frame");

        if (numberOfFrames > 1) {
            leftArrow.addActionListener(e -> wrapper.moveToPreviousFrame());
            rightArrow.addActionListener(e -> wrapper.moveToNextFrame());
        }
        else {
            leftArrow.setEnabled(false);
            rightArrow.setEnabled(false);
        }


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
        FontIcon cog = FontIcon.of(MaterialDesignC.COG, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        settings = new JButton(cog);
        settings.setToolTipText("Settings");

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
        if (numberOfFrames <= 1) { return; }
        updateFrameLabel(frameIndex);
        updateSlider(frameIndex);
        repaint();
    }

    private void updateFrameLabel(int frameIndex) {
        if (frameInfo == null) { return; }
        frameInfo.setText("Frame: " + frameIndex + "/" + (numberOfFrames - 1));
    }

    private void updateSlider(int frameIndex) {
        if (slider == null) { return;}
        slider.setValue(frameIndex);
    }

    @Override
    public void showSelected() {
        FontIcon select = FontIcon.of(MaterialDesignS.SELECT_ALL, DEFAULT_ICON_SIZE, DISABLED_ICON_COLOR);
        selectButton.setIcon(select);
        selectButton.setEnabled(false);
        selectButton.setToolTipText("This image is selected");
    }

    @Override
    public void showDeselected() {
        FontIcon select = FontIcon.of(MaterialDesignS.SELECT_ALL, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        selectButton.setIcon(select);
        selectButton.setEnabled(true);
        selectButton.setToolTipText("Select this image");
    }
}
