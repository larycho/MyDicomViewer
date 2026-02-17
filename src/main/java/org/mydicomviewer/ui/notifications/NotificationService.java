package org.mydicomviewer.ui.notifications;

public interface NotificationService {
    void showSuccessMessage(String title, String message);
    void showErrorMessage(String title, String message);
    void showInfoMessage(String title, String message);
}
