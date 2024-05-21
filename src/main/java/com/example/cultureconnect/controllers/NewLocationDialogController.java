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
import java.util.Objects;

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
    @FXML
    private Label opretNyLokationLabel;

    private Logic logic;
    private CultureConnectController cultureConnectController;

    private Lokation nuværendeLokation;
    private Person ansvarligPerson;

    public void initialize(){
        this.logic = Logic.getInstance();
        choiceBoxOptions();
        autoFillInfo();
        colorRandomizer();
        illegalColorChosen();
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
        if(opretNyLokationKnap.getText().equals("Opret")){
            String name = navnNyLokationFelt.getText();
            String email = emailNyLokationFelt.getText();
            String telefonnummer = telefonnummerNyLokationFelt.getText();
            Color color = lokationColorchooser.getValue();
            String farveKode = String.format("#%02X%02X%02X",
                    (int) (color.getRed() * 255),
                    (int) (color.getGreen() * 255),
                    (int) (color.getBlue() * 255) );
            String selectedAnsvarlig = ansvarligVælger.getValue();
            Person ansvarlig = logic.getPersons().stream().
                    filter(person -> person.getName().equals(selectedAnsvarlig)).
                    findFirst().orElse(null);

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

            if (ansvarlig != null) {
                logic.setAnsvarligForLocation(lok, ansvarlig);
            }

            Stage stage = (Stage) opretNyLokationKnap.getScene().getWindow();
            stage.close();
        } else{
            System.out.println("Gemmer");
            String name = navnNyLokationFelt.getText();
            String email = emailNyLokationFelt.getText();
            String telefonnummer = telefonnummerNyLokationFelt.getText();
            Color color = lokationColorchooser.getValue();
            String farveKode = String.format("#%02X%02X%02X",
                    (int) (color.getRed() * 255),
                    (int) (color.getGreen() * 255),
                    (int) (color.getBlue() * 255) );
            String selectedAnsvarlig = ansvarligVælger.getValue();
            Person ansvarlig = logic.getPersons().stream().
                    filter(person -> person.getName().equals(selectedAnsvarlig)).
                    findFirst().orElse(null);

            if (name == null || name.trim().isEmpty()) {
                locationMustHaveName();
                return;
            }


            nuværendeLokation.setDescription(email + "\n   " + telefonnummer);
            nuværendeLokation.setFarveKode(farveKode);

            if (ansvarligVælger != null && ansvarlig != null){
                ansvarlig.setErAnsvarlig(true);
                logic.updateUser(ansvarlig);
            }

            logic.updateLokation(nuværendeLokation, name);

            if (ansvarlig != ansvarligPerson && ansvarligPerson != null){
                logic.deleteAnsvarlig(nuværendeLokation, ansvarligPerson.getCPR());
            }

            Stage stage = (Stage) opretNyLokationKnap.getScene().getWindow();
            stage.close();
        }
    }

    public void locationMustHaveName(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fejl");
        alert.setHeaderText("Lokationen skal have et navn");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        dialogPane.getStyleClass().add("Alerts");
        alert.showAndWait();

    }

    public void locationExistsAlready() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fejl");
        alert.setHeaderText("Lokationen findes allerede");
        alert.setContentText("Du kan redigere lokationen i stedet for at oprette en ny");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        dialogPane.getStyleClass().add("Alerts");
        alert.showAndWait();
    }

    public void setMainController(CultureConnectController cultureConnectController) {
        this.cultureConnectController = cultureConnectController;
    }

    public void autoFillInfo(){
        ansvarligVælger.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            Person ansvarlig = logic.getPersons().stream().
                    filter(person -> person.getName().equals(newValue)).
                    findFirst().orElse(null);

            if (ansvarlig != null) {
                ansvarligEmailNyLokationFelt.setText(ansvarlig.getEmail());
                teansvarligTefonnummerNyLokationFelt.setText(String.valueOf(ansvarlig.getTlfNr()));
            }
        });
    }

    public void editInfo(Lokation lokation){
        nuværendeLokation = lokation;

        if (!Objects.equals(lokation.getDescription(), "\n")){
            String tlfOgEmail = lokation.getDescription();

            String[] parts = tlfOgEmail.split("\\s+");

            String email = parts.length >= 1 ? parts[0] : "";
            String phoneNumber = parts.length >= 2 ? parts[1] : "";

            emailNyLokationFelt.setText(email);
            telefonnummerNyLokationFelt.setText(phoneNumber);
        }

        opretNyLokationKnap.setText("Gem");
        opretNyLokationLabel.setText("Rediger Lokation");

        if (lokation.getAnsvarligPerson() != null){
            ansvarligPerson = lokation.getAnsvarligPerson();
        }
        lokationColorchooser.setValue(Color.valueOf(lokation.getFarveKode()));
        navnNyLokationFelt.setText(lokation.getName());
        navnNyLokationFelt.setEditable(false);

        if (ansvarligPerson == null) {
            ansvarligVælger.setValue("Vælg ansvarlig");
        }else{
            ansvarligVælger.setValue(ansvarligPerson.getName());
            ansvarligEmailNyLokationFelt.setText(ansvarligPerson.getEmail());
            teansvarligTefonnummerNyLokationFelt.setText(String.valueOf(ansvarligPerson.getTlfNr()));
        }

    }

    public void illegalColorChosen(){
        lokationColorchooser.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.getRed() > 0.9 && newValue.getGreen() > 0.9 && newValue.getBlue() > 0.9){
                lokationColorchooser.setValue(oldValue);
            }
            if (Math.abs(newValue.getRed() - newValue.getGreen()) < 0.01 && Math.abs(newValue.getGreen() - newValue.getBlue()) < 0.01){
                lokationColorchooser.setValue(oldValue);
            }
        });
    }

    public void colorRandomizer(){
        double red = Math.random();
        double green = Math.random();
        double blue = Math.random();
        lokationColorchooser.setValue(Color.color(red, green, blue));
    }
}
