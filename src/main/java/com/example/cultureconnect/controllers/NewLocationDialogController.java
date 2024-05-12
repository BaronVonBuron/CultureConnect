package com.example.cultureconnect.controllers;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.paint.Color;
import javafx.stage.Stage;


import java.util.List;

public class NewLocationDialogController {

    @FXML
    private TextField ansvarligEmailNyLokationFelt;

    @FXML
    private ChoiceBox<String> ansvarligVælger;

    @FXML
    private TextField emailNyLokationFelt;

    @FXML
    private TextField navnNyLokationFelt;

    @FXML
    private Button opretNyLokationKnap;

    @FXML
    private TextField teansvarligTefonnummerNyLokationFelt;

    @FXML
    private TextField telefonnummerNyLokationFelt;

    @FXML
    private ColorPicker lokationColorchooser;

    private Logic logic;
    private CultureConnectController cultureConnectController;

    public void initialize(){
        this.logic = Logic.getInstance();
        choiceBoxOptions();
    }

    public void choiceBoxOptions(){
        List<Person> persons = logic.getPersons();
        ObservableList<String> ansvarligeMuligheder = FXCollections.observableArrayList();
        for (Person person : persons) {
            ansvarligeMuligheder.add(person.getName());
        }
        ansvarligVælger.setItems(ansvarligeMuligheder);
        ansvarligVælger.setValue("Vælg ansvarlig");
    }

    @FXML
    void opretNyLokationKnapPressed(ActionEvent event) {
        String name = navnNyLokationFelt.getText();
        String email = emailNyLokationFelt.getText();
        String telefonnummer = telefonnummerNyLokationFelt.getText();
        Color color = lokationColorchooser.getValue();
        String farveKode = String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255) );

        if (name == null || name.trim().isEmpty()) {
            locationMustHaveName();
            return;
        }

        List<Lokation> alreadyThere = logic.getLocations();
        for (Lokation lok : alreadyThere){
            if (lok.getName().equals(name)){
                locationExistsAlready();
                return;
            }
        }

        Lokation lok = new Lokation(name, email + "\n   " + telefonnummer, farveKode);
        logic.createLocation(lok);

        Stage stage = (Stage) opretNyLokationKnap.getScene().getWindow();
        stage.close();
    }

    public void locationMustHaveName(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fejl");
        alert.setHeaderText("Lokationen skal have et navn");
        alert.showAndWait();
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        dialogPane.getStyleClass().add("Alerts");
    }

    public void locationExistsAlready() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fejl");
        alert.setHeaderText("Lokationen findes allerede");
        alert.setContentText("Du kan redigere lokationen i stedet for at oprette en ny");
        alert.showAndWait();
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        dialogPane.getStyleClass().add("Alerts");
    }

    public void setMainController(CultureConnectController cultureConnectController) {
        this.cultureConnectController = cultureConnectController;
    }
}
