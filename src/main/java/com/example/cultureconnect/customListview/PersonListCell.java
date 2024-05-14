package com.example.cultureconnect.customListview;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.controllers.CultureConnectController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.List;

public class PersonListCell extends ListCell {

    private Person person;
    private boolean isExpanded = false;
    private Label emailLabel = new Label();
    private Label phoneLabel = new Label();
    private Label nameLabel = new Label();
    private VBox vbox = new VBox();
    private HBox hbox = new HBox();
    private HBox hboxButtons = new HBox();
    private Button editButton = new Button("Rediger");
    private Button deleteButton = new Button("Slet");
    private Logic logic;


    public PersonListCell(Person person){
        super(); // Call the constructor of the superclass
        this.person = person;
        Circle frame = new Circle(30);
        Image image = new Image("https://www.w3schools.com/w3images/avatar2.png"); //placeholder if they have to picture
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(45);
        imageView.setFitWidth(45);
        frame.setStroke(javafx.scene.paint.Color.BLACK); //placeholder hvis der ikke er en lokation
        frame.setStrokeWidth(5);
        nameLabel.setText(person.getName());
        hbox.getChildren().addAll(imageView, nameLabel);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hbox.setSpacing(5);
        hboxButtons.getChildren().addAll(editButton, deleteButton);
        hboxButtons.setSpacing(5);
        hboxButtons.getStylesheets().add(getClass().getResource("/CultureConnectCSS.css").toExternalForm());
        deleteButton.setPrefWidth(75);
        deleteButton.setPrefHeight(35);
        editButton.setPrefWidth(75);
        editButton.setPrefHeight(35);
        deleteButton.getStyleClass().add("Buttons");
        editButton.getStyleClass().add("Buttons");
        setGraphic(hbox);
        setPrefSize(200, 50);
        setStyle("-fx-background-color: transparent");
        setOnMouseClicked(event -> {
            if (isExpanded) {
                vbox.getChildren().removeAll(emailLabel, phoneLabel, hboxButtons);
                setPrefSize(200, 50);
            } else {
                emailLabel.setText("Email: " + getEmail());
                phoneLabel.setText("Telefon: " + new String(getTlfNr()));
                vbox.getChildren().addAll(emailLabel, phoneLabel, hboxButtons);
                setPrefSize(200, 100);
            }
            isExpanded = !isExpanded;
        });
        vbox.getChildren().add(hbox);
        setGraphic(vbox);

        deleteButton.setOnMouseClicked(event -> {
            Person personClicked = this.getPerson();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("Er du sikker på at du vil slette " + personClicked.getName() + "?");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/CultureConnectCSS.css").toExternalForm());
            dialogPane.getStyleClass().add("Alerts");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.YES) {
                this.logic = Logic.getInstance();
                logic.deletePerson(personClicked);
            }
        });

        this.setOnDragDetected(event -> {
            System.out.println("drag detected person list cell");
            Dragboard db = this.startDragAndDrop(javafx.scene.input.TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(person.getName());
            db.setContent(content);
            event.consume();
        });
        this.setOnDragDone(event -> {
            // Check if the drag operation was successful
            if (event.getTransferMode() == TransferMode.MOVE) {

                System.out.println("drag done person list cell");
            }

            event.consume();
        });
    }

    public Person getPerson() {
        return person;
    }

    public String getName() {
        return person.getName();
    }

    public String getEmail() {
        return person.getEmail();
    }

    public char[] getTlfNr() {
        int number = person.getTlfNr();
        return Integer.toString(number).toCharArray();
    }
}