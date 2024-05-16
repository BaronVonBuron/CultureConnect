package com.example.cultureconnect.controllers;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.List;

public class NewUserDialogController {

    @FXML
    private ChoiceBox<String> arbejdspladsVælger;

    @FXML
    private TextField billedeNyBrugerFelt;

    @FXML
    private TextField emailNyBrugerFelt;

    @FXML
    private TextField navnNyBrugerFelt;

    @FXML
    private TextField cprNyBrugerFelt;

    @FXML
    private Button opretNyBrugerKnap;

    @FXML
    private TextField stillingNyBrugerFelt;

    @FXML
    private TextField telefonnummerNyBrugerFelt;

    @FXML
    private TextField kodeordNyBrugerFelt;

    @FXML
    private Button vælgFilKnap;

    @FXML
    private Label opretNyBrugerLabel;

    private Logic logic;

    private Person nuværendePerson;
    private Lokation arbejdsplads;

    public void initialize(){
        this.logic = Logic.getInstance();
        choiceBoxOptions();
    }

    public void choiceBoxOptions(){
        List<Lokation> locations = logic.getLocations();
        ObservableList<String> locationMuligheder = FXCollections.observableArrayList();
        for (Lokation location : locations) {
            locationMuligheder.add(location.getName());
        }
        arbejdspladsVælger.setItems(locationMuligheder);
        arbejdspladsVælger.setValue("Vælg arbejdsplads");
    }

    @FXML
    void opretNyBrugerKnapPressed(ActionEvent event) {

    }

    @FXML
    void vælgFilKnapPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vælg billed");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Image Files", "*png")); //TODO evt. tilføje flere filtyper
        File selectedFile = fileChooser.showOpenDialog(vælgFilKnap.getScene().getWindow());
        if (selectedFile != null){
            billedeNyBrugerFelt.setText(selectedFile.getAbsolutePath());
        }
    }

    public void editInfo(Person person){
        nuværendePerson = person;

        opretNyBrugerKnap.setText("Gem");
        opretNyBrugerLabel.setText("Rediger bruger");
        this.logic = Logic.getInstance();
        String arbejdspladsNavn = logic.findBrugersLokation(nuværendePerson.getCPR());
        arbejdsplads = logic.findLokation(arbejdspladsNavn);

        emailNyBrugerFelt.setText(nuværendePerson.getEmail());
        telefonnummerNyBrugerFelt.setText(String.valueOf(nuværendePerson.getTlfNr()));
        cprNyBrugerFelt.setText(nuværendePerson.getCPR());
        cprNyBrugerFelt.setEditable(false);
        navnNyBrugerFelt.setText(nuværendePerson.getName());
        stillingNyBrugerFelt.setText(logic.findBrugersStilling(nuværendePerson.getCPR()));
        kodeordNyBrugerFelt.setText(logic.findBrugerKodeord(nuværendePerson.getCPR()));
        billedeNyBrugerFelt.setText(nuværendePerson.getPicture().getUrl());

        if (arbejdsplads == null) {
            arbejdspladsVælger.setValue("Vælg arbejdsplads");
        }else{
            arbejdspladsVælger.setValue(arbejdsplads.getName());
        }

    }

}