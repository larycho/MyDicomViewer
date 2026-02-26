package org.mydicomviewer.ui;

import com.google.inject.Singleton;
import org.kordamp.ikonli.materialdesign2.MaterialDesignA;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

@Singleton
public class TagPanel extends JPanel {

    private JPanel collapsedPanel;

    private JPanel expandedPanel;

    private final CardLayout cardLayout;
    private JButton expandButton;
    private JButton collapseButton;
    private JScrollPane scrollPane;


    private final int DEFAULT_ICON_SIZE = 20;
    private final Color DEFAULT_ICON_COLOR = UIManager.getColor("Component.accentColor");

    public TagPanel() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        createCollapsedPanel();
        createExpandedPanel();
        setCardSides();

        setupActionListeners();
    }

    public void addTableToScrollPane(JTable table) {
        scrollPane.setViewportView(table);
    }

    public void clearScrollPane() {
        scrollPane.setViewportView(null);
    }

    private void createExpandedPanel() {
        expandedPanel = new JPanel();
        expandedPanel.setLayout(new BorderLayout());
        expandedPanel.setPreferredSize(new Dimension(300, 100));

        JPanel topOfExpandedPanel = createTopOfExpandedPanel();
        scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        expandedPanel.add(topOfExpandedPanel, BorderLayout.NORTH);
        expandedPanel.add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createTopOfExpandedPanel() {
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        // Creating title label
        JLabel title = new JLabel("Tag Viewer", SwingConstants.CENTER);

        // Creating 'collapse' button
        FontIcon icon = FontIcon.of(MaterialDesignA.ARROW_RIGHT_DROP_CIRCLE, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        collapseButton = new JButton(icon);
        collapseButton.setToolTipText("Hide Panel");

        // Adding them to the panel
        topPanel.add(collapseButton, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);

        // Makes the text label truly centered
        topPanel.add(Box.createHorizontalStrut(collapseButton.getPreferredSize().width), BorderLayout.EAST);

        return topPanel;
    }

    private void createCollapsedPanel() {
        collapsedPanel = new JPanel();
        FontIcon icon = FontIcon.of(MaterialDesignA.ARROW_LEFT_DROP_CIRCLE, DEFAULT_ICON_SIZE, DEFAULT_ICON_COLOR);
        expandButton = new JButton(icon);
        expandButton.setToolTipText("Show Tag Panel");
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
