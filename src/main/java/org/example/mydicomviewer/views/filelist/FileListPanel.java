package org.example.mydicomviewer.views.filelist;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.example.mydicomviewer.services.OpenFileManager;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Singleton
public class FileListPanel extends JPanel {

    private JPanel collapsedPanel;
    private JPanel expandedPanel;
    private final CardLayout cardLayout;

    private JPanel topPanel;
    private JButton expandButton;
    private JButton collapseButton;
    private FileListScrollPane scrollPane;
    private final OpenFileManager openFileManager;

    private final int DEFAULT_ICON_SIZE = 20;
    private final Color DEFAULT_ICON_COLOR = UIManager.getColor("Component.accentColor");

    @Inject
    public FileListPanel(OpenFileManager openFileManager) {

        this.openFileManager = openFileManager;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        createCollapsedPanel();
        createExpandedPanel();
        setCardSides();

        setupActionListeners();
    }

    public void clear() {
        scrollPane.clear();
        revalidate();
        repaint();
    }

    public void addFileToList(FileNodeData file) {
        scrollPane.addFile(file);
    }

    private void setupActionListeners() {
        collapseButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(this, "Collapsed");
            this.getParent().repaint();
            this.getParent().revalidate();
        });

        expandButton.addActionListener((ActionEvent e) -> {
            cardLayout.show(this, "Expanded");
            this.getParent().repaint();
            this.getParent().revalidate();
        });
    }

    private void setCardSides() {
        add(collapsedPanel, "Collapsed");
        add(expandedPanel, "Expanded");
    }

    private void createExpandedPanel() {

        initialExpandedPanelConfig();
        createSubPanelsOfExpandedPanel();
        addSubPanelsToExpandedPanel();
    }

    private void initialExpandedPanelConfig() {
        expandedPanel = new JPanel();
        expandedPanel.setLayout(new BorderLayout());
        expandedPanel.setPreferredSize(new Dimension(300, 100));
    }

    private void createSubPanelsOfExpandedPanel() {
        topPanel = createTopOfExpandedPanel();
        scrollPane = new FileListScrollPane(openFileManager);
    }

    private void addSubPanelsToExpandedPanel() {
        expandedPanel.add(topPanel, BorderLayout.NORTH);
        expandedPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTopOfExpandedPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Creating title label
        JLabel title = new JLabel("Opened DICOM Files", SwingConstants.CENTER);

        // Creating 'collapse' button
        FontIcon icon = FontIcon.of(MaterialDesignA.ARROW_LEFT_DROP_CIRCLE, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        collapseButton = new JButton(icon);
        collapseButton.setToolTipText("Hide panel");

        // Adding them to the panel
        topPanel.add(title, BorderLayout.CENTER);
        topPanel.add(collapseButton, BorderLayout.EAST);

        return topPanel;
    }

    private void createCollapsedPanel() {
        collapsedPanel = new JPanel();

        FontIcon icon = FontIcon.of(MaterialDesignA.ARROW_RIGHT_DROP_CIRCLE, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        expandButton = new JButton(icon);
        expandButton.setToolTipText("Expand file list panel");

        collapsedPanel.add(expandButton);
    }

    // Needed for this panel to resize according to the size of its content
    @Override
    public Dimension getPreferredSize() {
        for (Component c : getComponents()) {
            if (c.isVisible()) {
                return c.getPreferredSize();
            }
        }
        return super.getPreferredSize();
    }

    public void setExpanded() {
        cardLayout.show(this, "Expanded");
    }
}
