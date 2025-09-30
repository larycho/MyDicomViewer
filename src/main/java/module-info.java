module org.example.mydicomviewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires dcm4che.core;
    requires pixelmed;
    requires javafx.swing;
    requires com.formdev.flatlaf;


    opens org.example.mydicomviewer to javafx.fxml;
    opens org.example.mydicomviewer.controllers to javafx.fxml;
    exports org.example.mydicomviewer;
    exports org.example.mydicomviewer.models;
    opens org.example.mydicomviewer.models to javafx.fxml;
    exports org.example.mydicomviewer.processing.file;
    opens org.example.mydicomviewer.processing.file to javafx.fxml;
    exports org.example.mydicomviewer.display;
    opens org.example.mydicomviewer.display to javafx.fxml;
}