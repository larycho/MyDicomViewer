package org.example.mydicomviewer.services;

public interface NotificationService {
    void showSuccessMessage(String title, String message);
    void showErrorMessage(String title, String message);
    void showInfoMessage(String title, String message);
}
