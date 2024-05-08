package com.example.cultureconnect.controllers;

import com.example.cultureconnect.Logic.Logic;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CultureConnectLoginController {

    @FXML
    private TextField brugernavnFelt;

    @FXML
    private TextField kodeordFelt;

    @FXML
    private Button loginKnap;
    private Logic logic;


    public void initialize() {
        //add eventlistener to kodeordfelt, so that when enter is pressed, login is called
        kodeordFelt.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                login();
            }
        });

        //add eventlistener to brugernavnfelt, so that when enter is pressed, login is called
        brugernavnFelt.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                login();
            }
        });
    }

    @FXML
    void loginKnapPressed() {
        login();
    }

    public void login(){
        if (brugernavnFelt.getText().isEmpty() || kodeordFelt.getText().isEmpty()){
            System.out.println("Brugernavn eller kodeord er tomt");
        } else {
            String brugernavn = brugernavnFelt.getText();
            String kodeord = kodeordFelt.getText();
            if (logic.login(brugernavn, kodeord)) {
                System.out.println("Login success");
                System.out.println("Current user: " + logic.getCurrentUser().getEmail());
                // Get the current stage and close it
                Stage stage = (Stage) loginKnap.getScene().getWindow();
                stage.close();

            } else {
                System.out.println("Login failed");
            }
        }

    }

    public void setLogic(Logic logic) {
        this.logic = logic;
    }

}