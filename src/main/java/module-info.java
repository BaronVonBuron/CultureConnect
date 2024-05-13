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
    exports com.example.cultureconnect.Projekt;
    opens com.example.cultureconnect.Projekt to javafx.fxml;
    exports com.example.cultureconnect.Person;
    opens com.example.cultureconnect.Person to javafx.fxml;
    exports com.example.cultureconnect.Lokation;
    opens com.example.cultureconnect.Lokation to javafx.fxml;
    exports com.example.cultureconnect.calendar;
    opens com.example.cultureconnect.calendar to javafx.fxml;
    exports com.example.cultureconnect.databaseaccess;
    opens com.example.cultureconnect.databaseaccess to javafx.fxml;
    exports com.example.cultureconnect.Logic;
    opens com.example.cultureconnect.Logic to javafx.fxml;
    exports com.example.cultureconnect.customListview;
    opens com.example.cultureconnect.customListview to javafx.fxml;
}