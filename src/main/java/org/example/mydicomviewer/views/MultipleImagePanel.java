package org.example.mydicomviewer.views;

import com.formdev.flatlaf.FlatClientProperties;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.display.SplitScreenElement;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.services.ScreenModeProvider;
import org.example.mydicomviewer.services.SelectedImageManager;
import org.example.mydicomviewer.views.image.panel.ImagePanelWrapper;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class MultipleImagePanel extends JPanel {

    private SplitScreenMode mode;
    private final LinkedList<ImagePanelWrapper> wrappers = new LinkedList<>();
    private SelectedImageManager selectedImageManager;

    @Inject
    public MultipleImagePanel(ScreenModeProvider provider,
                              SelectedImageManager selectedImageManager) {
        this.mode = provider.getDefaultScreenMode();
        this.selectedImageManager = selectedImageManager;

        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);
        setVisible(true);
        updateDisplay();
    }

    public MultipleImagePanel(SplitScreenMode mode) {
        this.mode = mode;
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        setVisible(true);
        updateDisplay();
    }

    public void setMode(SplitScreenMode mode) {
        this.mode = mode;
    }

    public void setSelectedImage(ImagePanelWrapper selectedImage) {
        selectedImageManager.setSelectedImage(selectedImage);
    }
    // TODO
    public ImagePanelWrapper getSelectedImage() {
        ImagePanelWrapper wrapper = selectedImageManager.getSelectedImage();

        if (wrapper != null) {
            return wrapper;
        }

        if (!wrappers.isEmpty()) {
            return wrappers.getFirst();
        }

        return null;
    }

    public boolean areAnyImagesAdded() {
        return !wrappers.isEmpty();
    }

    public void setAndApplyMode(SplitScreenMode mode) {
        setMode(mode);
        updateDisplay();
    }

    public List<ImagePanelWrapper> getAllImages() {
        return wrappers;
    }


    public void addImage(ImagePanelWrapper wrapper) {
        int maxSlots = mode.getElements().size();

        if (wrappers.size() == maxSlots) {
            replaceLastPanel(wrapper);
        }
        else {
            addNewPanel(wrapper);
        }

        handleInvisibleSelection();
        updateDisplay();
    }

    private void handleInvisibleSelection() {
        ImagePanelWrapper selectedImage = selectedImageManager.getSelectedImage();

        if (wrappers.size() == 1) {
            selectedImage.showDeselected();
        }
        if (wrappers.size() > 1) {
            selectedImage.showSelected();
        }
    }

    private void replaceLastPanel(ImagePanelWrapper wrapper) {
        updateSelectionWhenReplacingPanel(wrapper);

        this.wrappers.removeLast();
        this.wrappers.add(wrapper);
    }

    private void updateSelectionWhenReplacingPanel(ImagePanelWrapper wrapper) {
        ImagePanelWrapper lastWrapper = wrappers.getLast();
        if (lastWrapper.equals(selectedImageManager.getSelectedImage())) {
            selectedImageManager.setSelectedImage(wrapper);
        }
    }

    private void addNewPanel(ImagePanelWrapper wrapper) {
        this.wrappers.add(wrapper);

        // Auto-select the panel if it's the first one added
        if (wrappers.size() == 1) {
            selectedImageManager.setSelectedImage(wrappers.getFirst());
        }
    }


    public void updateDisplay() {
        removeAll();

        List<SplitScreenElement> elements = mode.getElements();
        Iterator<ImagePanelWrapper> iterator = wrappers.iterator();

        for (SplitScreenElement element : elements) {
            // if there are still images to be added
            if (iterator.hasNext()) {
                ImagePanelWrapper wrapper = iterator.next();
                addImagePanel(wrapper.getPanel(), element);
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
        while (wrappers.size() > length) {
            ImagePanelWrapper wrapper = wrappers.removeLast();
            //remove((Component) wrapper);
            remove(wrapper.getPanel());
        }
    }

    public void clearDisplay() {
        for (ImagePanelWrapper wrapper : wrappers) {
            //remove((Component) image);
            remove(wrapper.getPanel());
        }
        wrappers.clear();
        updateDisplay();
    }


    private void addImagePanel(JPanel imagePanel, SplitScreenElement element) {
        GridBagConstraints constraints = new GridBagConstraints();
        setConstraintsParameters(constraints, element);

        imagePanel.setPreferredSize(new Dimension(0,0));
        add(imagePanel, constraints);
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
        ImagePanelWrapper wrapper = wrappers.getFirst();
        wrappers.removeFirst();
        wrappers.add(wrapper);

        removeAll();
        updateDisplay();
    }

    private void refresh() {
        revalidate();
        repaint();
        for (ImagePanelWrapper wrapper : wrappers) {
            wrapper.refresh();
        }
    }
}
