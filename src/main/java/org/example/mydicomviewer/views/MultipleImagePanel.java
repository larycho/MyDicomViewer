package org.example.mydicomviewer.views;

import org.example.mydicomviewer.display.SplitScreenElement;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.scijava.parsington.Main;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class MultipleImagePanel extends JPanel {

    private SplitScreenMode mode;
    private LinkedList<MainImagePanel> images;
    private MainImagePanel currentlySelectedImage = null;

    public MultipleImagePanel(SplitScreenMode mode) {
        this.mode = mode;
        this.images = new LinkedList<>();
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        setVisible(true);
        updateDisplay();
    }

    public void showSelectFilePrompt() {

        JLabel prompt = createSelectFilePrompt();
        add(prompt);

        refresh();
    }

    private JLabel createSelectFilePrompt() {
        JLabel prompt = new JLabel("Select a file to display: File > Open file...");
        prompt.setHorizontalAlignment(SwingConstants.CENTER);
        return prompt;
    }

    public void setMode(SplitScreenMode mode) {
        this.mode = mode;
    }

    public void setSelectedImage(MainImagePanel selectedImage) {
        currentlySelectedImage = selectedImage;
    }

    public MainImagePanel getSelectedImage() {
        if (currentlySelectedImage == null && !images.isEmpty()) {
            currentlySelectedImage = images.get(0);
        }
        return currentlySelectedImage;
    }

    public boolean areAnyImagesAdded() {
        return !images.isEmpty();
    }

    public void setAndApplyMode(SplitScreenMode mode) {
        setMode(mode);
        updateDisplay();
    }

    public void setImages(LinkedList<MainImagePanel> images) {
        this.images = images;
    }

    public void addImage(MainImagePanel image) {
        int maxSlots = mode.getElements().size();
        if (images.size() == maxSlots) {
            this.images.removeLast();
        }
        this.images.add(image);
        updateDisplay();
    }

    public void removeImage(MainImagePanel image) {
        boolean imageFound = this.images.remove(image);

        if (imageFound) {
            updateDisplay();
        }
    }

    public void updateDisplay() {
        removeAll();

        List<SplitScreenElement> elements = mode.getElements();
        Iterator<MainImagePanel> iterator = images.iterator();

        for (SplitScreenElement element : elements) {
            // if there are still images to be added
            if (iterator.hasNext()) {
                MainImagePanel image = iterator.next();
                addImagePanel(image, element);
            }
            // if not - add an empty panel
            else {
                addEmptyDisplayPanel(element);
            }
        }

        removeExcessImages(elements);
        refresh();
    }

    private void removeExcessImages(List<SplitScreenElement> elements) {
        int length = elements.size();
        while (images.size() > length) {
            MainImagePanel image = images.removeLast();
            remove(image);
        }
    }

    private void addImagePanel(MainImagePanel image, SplitScreenElement element) {
        GridBagConstraints constraints = new GridBagConstraints();
        setConstraintsParameters(constraints, element);
        add(image, constraints);
    }

    private void addEmptyDisplayPanel(SplitScreenElement element) {
        GridBagConstraints constraints = new GridBagConstraints();
        setConstraintsParameters(constraints, element);
        JPanel emptyPanel = createEmptyPanel();
        add(emptyPanel, constraints);
        // TODO
    }

    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.setBackground(Color.GRAY);
        panel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));

        JLabel label = new JLabel("Add new image: File > Open file...");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        panel.add(label);

        return panel;
    }

    private static void setConstraintsParameters(GridBagConstraints constraints, SplitScreenElement element) {
        constraints.fill = GridBagConstraints.BOTH;
        constraints.gridx = element.getX();
        constraints.gridy = element.getY();
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = element.getWidth();
        constraints.gridheight = element.getHeight();
    }

    public void rotate() {
        MainImagePanel image = images.getFirst();
        images.removeFirst();
        images.add(image);

        removeAll();
        updateDisplay();
    }

    private void refresh() {
        revalidate();
        repaint();
        for (MainImagePanel image : images) {
            image.refresh();
        }
    }
}
