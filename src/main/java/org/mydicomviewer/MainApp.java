package org.mydicomviewer;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.mydicomviewer.plugin.StartupAction;
import org.mydicomviewer.ui.MainWindow;

import java.util.Set;

@Singleton
public class MainApp {

    private final MainWindow mainWindow;
    private final Set<StartupAction> startupActions;

    @Inject
    public MainApp(
            MainWindow mainWindow,
            Set<StartupAction> startupActionSet
    ) {
        this.mainWindow = mainWindow;
        this.startupActions = startupActionSet;
    }

    public void start() {
        runStartupActions();
        mainWindow.setVisible(true);
    }

    private void runStartupActions() {
        for (StartupAction startupAction : startupActions) {
            startupAction.execute();
        }
    }
}
