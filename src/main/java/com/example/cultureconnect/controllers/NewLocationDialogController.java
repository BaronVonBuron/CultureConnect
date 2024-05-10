package com.example.cultureconnect.controllers;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Person.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

    }

}
