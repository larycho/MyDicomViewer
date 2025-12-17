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
    //private LinkedList<SingularImagePanelInt> images = new LinkedList<>();
    private LinkedList<ImagePanelWrapper> wrappers = new LinkedList<>();
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
        return selectedImageManager.getSelectedImage();
    }

    public void deselectCurrentImage() {
        selectedImageManager.deselectImage();
    }

    public boolean areAnyImagesAdded() {
        //return !images.isEmpty();
        return wrappers.isEmpty();
    }

    public void setAndApplyMode(SplitScreenMode mode) {
        setMode(mode);
        updateDisplay();
    }

//    public void addImage(SingularImagePanelInt image) {
//        int maxSlots = mode.getElements().size();
//        if (images.size() == maxSlots) {
//            this.images.removeLast();
//        }
//        this.images.add(image);
//        updateDisplay();
//    }

    public void addImage(ImagePanelWrapper wrapper) {
        int maxSlots = mode.getElements().size();
        if (wrappers.size() == maxSlots) {
            this.wrappers.removeLast();
        }
        this.wrappers.add(wrapper);
        updateDisplay();
    }

//    public void removeImage(SingularImagePanel image) {
//        boolean imageFound = this.images.remove(image);
//
//        if (imageFound) {
//            updateDisplay();
//        }
//    }

    public void updateDisplay() {
        removeAll();

        List<SplitScreenElement> elements = mode.getElements();
        //Iterator<SingularImagePanelInt> iterator = images.iterator();
        Iterator<ImagePanelWrapper> iterator = wrappers.iterator();

        for (SplitScreenElement element : elements) {
            // if there are still images to be added
            if (iterator.hasNext()) {
                //SingularImagePanelInt image = iterator.next();
                ImagePanelWrapper wrapper = iterator.next();
                //addImagePanel(image, element);
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
    }

//    private void addImagePanel(SingularImagePanelInt image, SplitScreenElement element) {
//        GridBagConstraints constraints = new GridBagConstraints();
//        setConstraintsParameters(constraints, element);
//        Component imageComponent = (Component) image;
//        imageComponent.setPreferredSize(new Dimension(0,0));
//        add(imageComponent, constraints);
//    }

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
