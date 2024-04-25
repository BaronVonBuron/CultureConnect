module com.example.cultureconnect {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.cultureconnect to javafx.fxml;
    exports com.example.cultureconnect;
    exports com.example.cultureconnect.main;
    opens com.example.cultureconnect.main to javafx.fxml;
}