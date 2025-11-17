package org.example.mydicomviewer;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.example.mydicomviewer.modules.MainAppModule;
import org.example.mydicomviewer.views.MainWindow;

import javax.swing.*;

public class MyDicomViewer {

    public static void main(String[] args) {
        setupLookAndFeel();

        Injector injector = Guice.createInjector(new MainAppModule());
        MainApp app = injector.getInstance(MainApp.class);
        app.start();
    }

    private static void showMainWindow() {
        MainWindow window = new MainWindow();
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);
    }

    private static void setupLookAndFeel() {
        //FlatDarkLaf.setup();
        FlatIntelliJLaf.setup();
    }
}
