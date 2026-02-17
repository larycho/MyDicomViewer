package org.mydicomviewer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.ui.MainWindow;


@Singleton
public class MainApp {

    private final MainWindow mainWindow;

    @Inject
    public MainApp(
            MainWindow mainWindow) {
            this.mainWindow = mainWindow;
    }

    public void start() {
        mainWindow.setVisible(true);
    }

}
