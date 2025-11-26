package org.example.mydicomviewer.views;

import com.formdev.flatlaf.FlatClientProperties;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.display.SplitScreenElement;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.services.ScreenModeProvider;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class MultipleImagePanel extends JPanel {

    private SplitScreenMode mode;
    private LinkedList<SingularImagePanel> images = new LinkedList<>();
    private SingularImagePanel currentlySelectedImage = null;

    @Inject
    public MultipleImagePanel(ScreenModeProvider provider) {
        this.mode = provider.getDefaultScreenMode();

        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        setVisible(true);
        updateDisplay();
    }

    public MultipleImagePanel(SplitScreenMode mode) {
        this.mode = mode;
        this.images = new LinkedList<>();
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        setVisible(true);
        updateDisplay();
    }

    public void setMode(SplitScreenMode mode) {
        this.mode = mode;
    }

    public void setSelectedImage(SingularImagePanel selectedImage) {
        currentlySelectedImage = selectedImage;
    }

    public SingularImagePanel getSelectedImage() {
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

    public void setImages(LinkedList<SingularImagePanel> images) {
        this.images = images;
    }

    public void addImage(SingularImagePanel image) {
        int maxSlots = mode.getElements().size();
        if (images.size() == maxSlots) {
            this.images.removeLast();
        }
        this.images.add(image);
        updateDisplay();
    }

    public void removeImage(SingularImagePanel image) {
        boolean imageFound = this.images.remove(image);

        if (imageFound) {
            updateDisplay();
        }
    }

    public void updateDisplay() {
        removeAll();

        List<SplitScreenElement> elements = mode.getElements();
        Iterator<SingularImagePanel> iterator = images.iterator();

        for (SplitScreenElement element : elements) {
            // if there are still images to be added
            if (iterator.hasNext()) {
                SingularImagePanel image = iterator.next();
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
            SingularImagePanel image = images.removeLast();
            remove(image);
        }
    }

    private void addImagePanel(SingularImagePanel image, SplitScreenElement element) {
        GridBagConstraints constraints = new GridBagConstraints();
        setConstraintsParameters(constraints, element);
        image.setPreferredSize(new Dimension(0,0));
        add(image, constraints);
    }

    private void addEmptyDisplayPanel(SplitScreenElement element) {
        GridBagConstraints constraints = new GridBagConstraints();
        setConstraintsParameters(constraints, element);
        JPanel emptyPanel = createEmptyPanel();
        emptyPanel.setPreferredSize(new Dimension(0,0));
        add(emptyPanel, constraints);
    }

    private JPanel createEmptyPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        panel.setBackground(Color.BLACK);
        panel.putClientProperty(FlatClientProperties.STYLE,
                "border: 1,1,1,1, $Component.borderColor"
        );

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
        SingularImagePanel image = images.getFirst();
        images.removeFirst();
        images.add(image);

        removeAll();
        updateDisplay();
    }

    private void refresh() {
        revalidate();
        repaint();
        for (SingularImagePanel image : images) {
            image.refresh();
        }
    }
}
