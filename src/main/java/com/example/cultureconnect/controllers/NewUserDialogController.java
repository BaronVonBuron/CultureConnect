package com.example.cultureconnect.controllers;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.customListview.PersonListCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

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
    private CultureConnectController cultureConnectController;

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
        if (nuværendePerson == null) {
            String name = navnNyBrugerFelt.getText();
            String email = emailNyBrugerFelt.getText();
            String tlfNrText = telefonnummerNyBrugerFelt.getText();
            Integer tlfNr = null;
            if (tlfNrText != null && !tlfNrText.trim().isEmpty()) {
                tlfNr = Integer.parseInt(tlfNrText.trim()); // Parse if not empty
            }
            String picture = billedeNyBrugerFelt.getText();
            Image image = null;
            if (picture != null && !picture.trim().isEmpty()) {
                image = new Image(new File(picture).toURI().toString());
            }
            String CPR = cprNyBrugerFelt.getText();
            String position = stillingNyBrugerFelt.getText();
            String kode = kodeordNyBrugerFelt.getText();
            String location = arbejdspladsVælger.getValue();

            if (tlfNr == null || tlfNrText.trim().isEmpty()) {
                tlfNr = 0;
            }
            if (picture == null || picture.trim().isEmpty()) {
                image = new Image("file:src/main/resources/images/avatar.png");
            }

            Person user = new Person(name, email, tlfNr, image, CPR);
            user.setKode(kode);
            user.setPosition(position);
            user.setLokation(logic.getLocations().stream().
                    filter(l -> l.getName().equals(location)).
                    findFirst().orElse(null));

            if (CPR == null || CPR.trim().isEmpty()) {
                personNeedsCpr();
                return;
            }
            if (name == null || name.trim().isEmpty()) {
                personNeedName();
                return;
            }
            //sørger for alle brugere har login info, hvis felterne ikke udfyldes:
            if (email == null || email.trim().isEmpty()) {
                user.setEmail(name + "@mail.dk");
            }
            if (kode == null || kode.trim().isEmpty()) {
                user.setKode(name);
            }
            logic.createUser(user);
        } else {
            String name = navnNyBrugerFelt.getText();
            String email = emailNyBrugerFelt.getText();
            String tlfNrText = telefonnummerNyBrugerFelt.getText();
            Integer tlfNr = null;
            if (tlfNrText != null && !tlfNrText.trim().isEmpty()) {
                tlfNr = Integer.parseInt(tlfNrText.trim()); // Parse if not empty
            }
            String picture = billedeNyBrugerFelt.getText();
            Image image = nuværendePerson.getPicture();
            if (picture != null && !picture.trim().isEmpty()) {
                image = new Image(new File(picture).toURI().toString());
            }

            String CPR = cprNyBrugerFelt.getText();
            String position = stillingNyBrugerFelt.getText();
            String kode = kodeordNyBrugerFelt.getText();
            String location = arbejdspladsVælger.getValue();

            if (tlfNr == null || tlfNrText.trim().isEmpty()) {
                tlfNr = 0;
            }

            nuværendePerson.setName(name);
            nuværendePerson.setEmail(email);
            nuværendePerson.setTlfNr(tlfNr);
            nuværendePerson.setPicture(image);
            nuværendePerson.setPosition(position);
            nuværendePerson.setKode(kode);
            nuværendePerson.setLokation(logic.getLocations().stream().
                    filter(l -> l.getName().equals(location)).
                    findFirst().orElse(null));

            if (CPR == null || CPR.trim().isEmpty()) {
                personNeedsCpr();
                return;
            }
            if (name == null || name.trim().isEmpty()) {
                personNeedName();
                return;
            }
            //sørger for alle brugere har login info, hvis felterne ikke udfyldes:
            if (email == null || email.trim().isEmpty()) {
                nuværendePerson.setEmail(name + "@mail.dk");
            }
            if (kode == null || kode.trim().isEmpty()) {
                nuværendePerson.setKode(name);
            }

            logic.updateUser(nuværendePerson);
        }


        Stage stage = (Stage) opretNyBrugerKnap.getScene().getWindow();
        stage.close();
    }

    @FXML
    void vælgFilKnapPressed(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Vælg billed");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter(
                "Image Files", "*.png")); //TODO evt. tilføje flere filtyper
        File selectedFile = fileChooser.showOpenDialog(vælgFilKnap.getScene().getWindow());
        if (selectedFile != null && selectedFile.getName().endsWith(".png")){
            billedeNyBrugerFelt.setText(selectedFile.getAbsolutePath());
        }
    }

    public void personNeedsCpr(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fejl");
        alert.setHeaderText("Brugere skal have et CPR nummer");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        dialogPane.getStyleClass().add("Alerts");
        alert.showAndWait();
    }

    public void personNeedName(){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fejl");
        alert.setHeaderText("Brugere skal have et navn");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        dialogPane.getStyleClass().add("Alerts");
        alert.showAndWait();
    }

    public void setMainController(CultureConnectController cultureConnectController) {
        this.cultureConnectController = cultureConnectController;
    }
    public void editInfo(Person person){
        nuværendePerson = person;
        String kodeord = logic.findBrugerKodeord(person.getCPR());
        System.out.println(kodeord);
        opretNyBrugerKnap.setText("Gem");
        opretNyBrugerLabel.setText("Rediger bruger");
        String arbejdspladsNavn = logic.findBrugersLokation(nuværendePerson.getCPR());
        arbejdsplads = logic.findLokation(arbejdspladsNavn);

        emailNyBrugerFelt.setText(nuværendePerson.getEmail());
        telefonnummerNyBrugerFelt.setText(String.valueOf(nuværendePerson.getTlfNr()));
        cprNyBrugerFelt.setText(nuværendePerson.getCPR());
        cprNyBrugerFelt.setEditable(false);
        navnNyBrugerFelt.setText(nuværendePerson.getName());
        stillingNyBrugerFelt.setText(logic.findBrugersStilling(nuværendePerson.getCPR()));
        kodeordNyBrugerFelt.setText(logic.findBrugerKodeord(nuværendePerson.getCPR()));

        if (arbejdsplads == null) {
            arbejdspladsVælger.setValue("Vælg arbejdsplads");
        }else{
            arbejdspladsVælger.setValue(arbejdsplads.getName());
        }

    }
}