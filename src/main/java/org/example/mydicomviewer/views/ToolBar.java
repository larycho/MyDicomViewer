package org.example.mydicomviewer.views;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.display.SplitScreenMode;
import org.example.mydicomviewer.listeners.ImageDisplayer;
import org.example.mydicomviewer.listeners.ResliceDisplayer;
import org.example.mydicomviewer.listeners.VolumeDisplayer;
import org.example.mydicomviewer.services.ScreenModeProvider;
import org.example.mydicomviewer.services.ScreenModeProviderImpl;
import org.example.mydicomviewer.services.SelectedImageManager;
import org.example.mydicomviewer.views.image.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Set;

@Singleton
public class ToolBar extends JToolBar {

    private JButton previous;
    private JButton next;
    private JButton manualWindowing;
    private JToggleButton drawButton;
    private DrawingOverlayPanel drawingOverlay;
    private JButton reslice;
    private JButton volume3D;
    private JButton modes;

    private Set<DrawingTool> tools;
    private ImageDisplayer imageDisplayer;
    private ResliceDisplayer resliceDisplayer;
    private SelectedImageManager selectedImageManager;

    @Inject
    public ToolBar(ImageDisplayer imageDisplayer,
                   ResliceDisplayer resliceDisplayer,
                   Set<DrawingTool> pluginTools,
                   SelectedImageManager selectedImageManager) {
        super();

        createToolList(pluginTools);

        this.selectedImageManager = selectedImageManager;

        addFrameChangeArrows();
        addWindowingButtons();
        addDrawingButtons();
        addResliceButton();
        addScreenModeButton();
        addDrawingTools();

        this.imageDisplayer = imageDisplayer;
        this.resliceDisplayer = resliceDisplayer;
        setupListeners();
    }

    private void createToolList(Set<DrawingTool> pluginTools) {
        tools = new HashSet<DrawingTool>();

        tools.add(new OvalTool());
        tools.add(new PencilTool());
        tools.add(new LineTool());

        tools.addAll(pluginTools);
    }

    private void setupListeners() {
        addScreenModeListeners(imageDisplayer);
        addResliceListeners(resliceDisplayer);
        addFrameChangeListeners(imageDisplayer);
        addWindowingListeners(imageDisplayer);
    }

    private void addScreenModeButton() {
        modes = new JButton("Modes");
        add(modes);
    }

    public void addScreenModeListeners(ImageDisplayer imageDisplayer) {
        ScreenModeProvider screenModeProvider = new ScreenModeProviderImpl();
        List<SplitScreenMode> modes = screenModeProvider.getAvailableScreenModes();

        JPopupMenu popupMenu = new JPopupMenu();
        for (SplitScreenMode mode : modes) {
            JMenuItem item = new JMenuItem(mode.toString());
            item.addActionListener(e -> imageDisplayer.changeScreenMode(mode));
            popupMenu.add(item);
        }

        this.modes.addActionListener(e -> popupMenu.show(this.modes, 0, this.modes.getHeight()));
    }

    private void addResliceButton() {
        ImageIcon resliceIcon = createImageIcon("src/main/resources/org/example/mydicomviewer/icons/3d-model.png");
        reslice = new JButton(resliceIcon);
        reslice.setToolTipText("Reslice");
        add(reslice);
    }

    private ImageIcon createImageIcon(String path) {
        File file = new File(path);
        try {
            BufferedImage src = ImageIO.read(file);
            BufferedImage image = new BufferedImage(40, 40, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = image.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);

            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawImage(src, 0, 0, 40, 40, null);
            g2d.dispose();
            return new ImageIcon(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addResliceListeners(ResliceDisplayer resliceDisplayer) {
        reslice.addActionListener(e -> resliceDisplayer.display());
    }

    public void add3DListeners(VolumeDisplayer volumeDisplayer) {
        reslice.addActionListener(e -> volumeDisplayer.display());
    }

    private void addDrawingButtons() {
        ImageIcon draw = createImageIcon("src/main/resources/org/example/mydicomviewer/icons/pencil.png");
        drawButton = new JToggleButton(draw);
        drawButton.setToolTipText("Draw Pencil");
        drawButton.setSelected(false);
//        drawButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                boolean drawMode = ((JToggleButton) e.getSource()).isSelected();
//                drawingOverlay.setDrawMode(drawMode);
//            }
//        });
        add(drawButton);
    }

    private void addDrawingTools() {
        addSeparator();
        JLabel toolLabel = new JLabel("Select tool:");
        add(toolLabel);

        JComboBox<DrawingTool> tools = new JComboBox<>();

        for (DrawingTool tool : this.tools) {
            tools.addItem(tool);
        }

        JButton apply = new JButton("Apply drawing tool");
        apply.addActionListener(e -> {
            DrawingTool selectedTool = (DrawingTool) tools.getSelectedItem();
            if (selectedTool != null) {
                selectedImageManager.setDrawingTool(selectedTool);
            }
        });

        JButton settings = new JButton("Settings");

        JPopupMenu popupMenu = new JPopupMenu();
        JCheckBoxMenuItem everyFrame = new JCheckBoxMenuItem("Draw shapes separately for every frame");
        everyFrame.setSelected(false);
        everyFrame.addActionListener(e -> {
        });

        popupMenu.add(everyFrame);

        settings.addActionListener(e -> popupMenu.show(settings, 0, settings.getHeight()));

        tools.setMaximumSize(tools.getPreferredSize());
        tools.setMaximumRowCount(10);
        add(tools);
        add(apply);
        add(settings);
        addSeparator();
    }

    public void setDrawingOverlay(DrawingOverlayPanel drawingOverlay) {
        this.drawingOverlay = drawingOverlay;
    }

    private void addWindowingButtons() {
        ImageIcon draw = new ImageIcon("src/main/resources/org/example/mydicomviewer/icons/windowing.png");
        manualWindowing = new JButton(draw);
        manualWindowing.setToolTipText("Manual windowing");
        add(manualWindowing);
    }

    private void addFrameChangeArrows() {
        ImageIcon rightArrow = createImageIcon("src/main/resources/org/example/mydicomviewer/icons/next.png");
        ImageIcon leftArrow = createImageIcon("src/main/resources/org/example/mydicomviewer/icons/previous.png");

        previous = new JButton(leftArrow);
        next = new JButton(rightArrow);

        previous.setToolTipText("Previous frame");
        next.setToolTipText("Next frame");

        add(previous);
        add(next);
    }

    public void addFrameChangeListeners(ImageDisplayer imageDisplayer) {
        previous.addActionListener(e -> imageDisplayer.previousFrame());
        next.addActionListener(e -> imageDisplayer.nextFrame());
    }

    public void addWindowingListeners(ImageDisplayer imageDisplayer) {
        manualWindowing.addActionListener(e -> {
            WindowingPopup popup = new WindowingPopup();
            int result = JOptionPane.showConfirmDialog(null, popup, "Manual Windowing", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                changeWindowParameters(imageDisplayer, popup);
            }

        });
    }

    private void changeWindowParameters(ImageDisplayer imageDisplayer, WindowingPopup popup) {
        double center = popup.getWindowCenter();
        double width = popup.getWindowWidth();
        imageDisplayer.setWindowing(center, width);
    }


}
