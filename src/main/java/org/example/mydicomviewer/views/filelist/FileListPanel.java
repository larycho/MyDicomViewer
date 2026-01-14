package org.example.mydicomviewer.views.filelist;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import org.example.mydicomviewer.services.OpenFileManager;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.*;
import java.awt.event.ActionEvent;

@Singleton
public class FileListPanel extends JPanel {

    private JPanel collapsedPanel;
    private JPanel expandedPanel;
    private CardLayout cardLayout;
    private JButton expandButton;
    private JButton collapseButton;
    private FileListScrollPane scrollPane;
    private OpenFileManager openFileManager;

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

    public FileListState getState() {
        return scrollPane.getState();
    }

    public void addFileToList(DefaultMutableTreeNode node) {
        scrollPane.addNode(node);
    }

    public void addFileToList(DefaultMutableTreeNode node, DefaultMutableTreeNode parent) {
        scrollPane.addNode(node, parent);
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
        expandedPanel = new JPanel();
        expandedPanel.setLayout(new BoxLayout(expandedPanel, BoxLayout.Y_AXIS));
        expandedPanel.setPreferredSize(new Dimension(300, 100));

        collapseButton = new JButton("Hide panel");

        scrollPane = new FileListScrollPane(openFileManager);
        expandedPanel.add(collapseButton);
        expandedPanel.add(scrollPane);

        collapseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void createCollapsedPanel() {
        collapsedPanel = new JPanel();
        expandButton = new JButton(">");
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
}
