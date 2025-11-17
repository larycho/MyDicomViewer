package org.example.mydicomviewer.views;

import com.google.inject.Singleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Singleton
public class TagPanel extends JPanel {

    private final int EXPANDED_WIDTH = 300;

    private JPanel collapsedPanel;
    private JPanel expandedPanel;
    private CardLayout cardLayout;
    private JButton expandButton;
    private JButton collapseButton;
    private JScrollPane scrollPane;

    public TagPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        createCollapsedPanel();
        createExpandedPanel();
        setCardSides();

        setupActionListeners();
    }

    public void addTabletoScrollPane(JTable table) {
        scrollPane.setViewportView(table);
    }

    private void refresh() {
        repaint();
        revalidate();
    }

    private void createExpandedPanel() {
        expandedPanel = new JPanel();
        expandedPanel.setLayout(new BoxLayout(expandedPanel, BoxLayout.Y_AXIS));
        expandedPanel.setPreferredSize(new Dimension(300, 100));

        collapseButton = new JButton("Hide panel");
        scrollPane = new JScrollPane();
        expandedPanel.add(collapseButton);
        expandedPanel.add(scrollPane);

        collapseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void createCollapsedPanel() {
        collapsedPanel = new JPanel();
        expandButton = new JButton("<");
        collapsedPanel.add(expandButton);
    }

    private void setCardSides() {
        add(collapsedPanel, "Collapsed");
        add(expandedPanel, "Expanded");
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
