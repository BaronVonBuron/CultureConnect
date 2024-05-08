module com.example.cultureconnect {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.swing;


    exports com.example.cultureconnect.main;
    opens com.example.cultureconnect.main to javafx.fxml;
    exports com.example.cultureconnect.controllers;
    opens com.example.cultureconnect.controllers to javafx.fxml;
}