package org.mydicomviewer.workers;

import com.google.inject.Inject;
import org.mydicomviewer.processing.io.export.SaveManager;
import org.mydicomviewer.processing.io.export.SaveParams;
import org.mydicomviewer.ui.notifications.NotificationService;

import javax.swing.*;
import java.util.concurrent.ExecutionException;

public class SaveFileWorker extends SwingWorker<Void, Void> {

    private SaveParams saveParams;
    private final NotificationService notificationService;
    private final SaveManager saveManager;

    @Inject
    public SaveFileWorker(NotificationService notificationService,
                          SaveManager saveManager) {
        this.notificationService = notificationService;
        this.saveManager = saveManager;
    }

    public void setSaveParams(SaveParams saveParams) {
        this.saveParams = saveParams;
    }

    @Override
    protected Void doInBackground() throws Exception {
        saveManager.save(saveParams);
        return null;
    }

    @Override
    protected void done() {
        try {
            get();
            notificationService.showSuccessMessage("Success", "Saving was successful.");
        } catch (ExecutionException | InterruptedException e) {
            notificationService.showErrorMessage("Error", "Saving failed.");
        }
    }
}
