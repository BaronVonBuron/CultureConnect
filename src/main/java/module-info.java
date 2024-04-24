module com.example.cultureconnect {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cultureconnect to javafx.fxml;
    exports com.example.cultureconnect;
}