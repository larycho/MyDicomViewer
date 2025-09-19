module org.example.mydicomviewer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.mydicomviewer to javafx.fxml;
    opens org.example.mydicomviewer.controllers to javafx.fxml;
    exports org.example.mydicomviewer;
    exports org.example.mydicomviewer.models;
    opens org.example.mydicomviewer.models to javafx.fxml;
}