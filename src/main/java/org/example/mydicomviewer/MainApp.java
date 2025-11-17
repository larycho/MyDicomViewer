package org.example.mydicomviewer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.example.mydicomviewer.views.MainWindow;

@Singleton
public class MainApp {

    private final MainWindow mainWindow;

    @Inject
    public MainApp(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public void start() {
        mainWindow.setVisible(true);
    }
}
