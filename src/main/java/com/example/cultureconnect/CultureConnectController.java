package com.example.cultureconnect;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CultureConnectController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}