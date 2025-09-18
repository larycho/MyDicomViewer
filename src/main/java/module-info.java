module org.example.mydicomviewer {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.mydicomviewer to javafx.fxml;
    opens org.example.mydicomviewer.controllers to javafx.fxml;
    exports org.example.mydicomviewer;
}