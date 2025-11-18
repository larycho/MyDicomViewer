package org.example.mydicomviewer.views;

import com.google.inject.Singleton;

import javax.swing.*;
import java.awt.*;

@Singleton
public class Footer extends JPanel {

    private JLabel footerLabel;
    private JProgressBar progressBar;

    public Footer() {
        setLayout(new FlowLayout(FlowLayout.RIGHT));
        setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        createFooterLabel();
    }

    private void createFooterLabel() {
        footerLabel = new JLabel("Ready | My Dicom Viewer ");
        add(footerLabel);
    }

    public void setLabelText(String text) {
        footerLabel.setText(text);
    }

    public void startProgress() {
        progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        add(progressBar);
    }

    public void stopProgress() {
        progressBar.setIndeterminate(false);
        remove(progressBar);
    }
}
