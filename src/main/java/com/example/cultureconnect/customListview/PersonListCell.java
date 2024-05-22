package com.example.cultureconnect.customListview;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.controllers.CultureConnectController;
import com.example.cultureconnect.controllers.CultureConnectLoginController;
import com.example.cultureconnect.controllers.NewUserDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

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
    private Color color;
    private Boolean admin;




    public PersonListCell(Person person){
        super(); // Call the constructor of the superclass
        this.person = person;
        this.logic = Logic.getInstance();

        Circle frame = new Circle(30);
        Image image = person.getPicture();
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(60);
        imageView.setFitWidth(60);
        Lokation lokation = person.getLokation();
        if (lokation != null) {
            String farveKode = lokation.getFarveKode();
            if (farveKode != null) {
                frame.setStroke(Color.web(farveKode));
            } else {
                frame.setStroke(Color.GREY); // default color if farveKode is null
            }
        } else {
            frame.setStroke(Color.GREY); // default color if lokation is null
        }        frame.setStrokeWidth(10);
        frame.setFill(Color.TRANSPARENT);

        imageView.setClip(new Circle(30, 30, 30));

        StackPane stackPane = new StackPane();
        stackPane.getChildren().addAll(frame, imageView);
        nameLabel.setText(person.getName());
        hbox.getChildren().addAll(stackPane, nameLabel);
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
        setPrefSize(195, 50);
        setStyle("-fx-background-color: transparent");
        setOnMouseClicked(event -> {
            findAdmin();
            if (isExpanded) {
                if (admin) {
                    vbox.getChildren().removeAll(emailLabel, phoneLabel, hboxButtons);
                    setPrefSize(195, 50);
                } else {
                    vbox.getChildren().removeAll(emailLabel, phoneLabel);
                    setPrefSize(195, 50);
                }
            } else {
                if (admin) {
                    emailLabel.setText("Email: " + getEmail());
                    phoneLabel.setText("Telefon: " + new String(getTlfNr()));
                    vbox.getChildren().addAll(emailLabel, phoneLabel, hboxButtons);
                    setPrefSize(195, 100);
                } else {
                    emailLabel.setText("Email: " + getEmail());
                    phoneLabel.setText("Telefon: " + new String(getTlfNr()));
                    vbox.getChildren().addAll(emailLabel, phoneLabel);
                    setPrefSize(195, 100);

                }
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

        editButton.setOnMouseClicked(event -> {
            Person personClicked = this.getPerson();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cultureconnect/NewUserDialog.fxml"));
                Stage editLokation = new Stage();
                editLokation.setTitle("Rediger Bruger");
                AnchorPane editLayout = loader.load();
                Scene scene = new Scene(editLayout);
                editLokation.setScene(scene);
                editLokation.setResizable(false);

                // Get the controller instance from the FXMLLoader
                NewUserDialogController userController = loader.getController();

                // Call a method to initialize the controller with the clicked location
                userController.editInfo(personClicked);

                editLokation.show();
            } catch (IOException e) {
                System.out.println("Can't open edit location window");
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

    public void findAdmin() {
        String userOrAdminNumber = logic.isAdmin(CultureConnectController.currentUser.getEmail());
        System.out.println("User or Admin Number: " + userOrAdminNumber);
        if (Objects.equals(userOrAdminNumber, "1")) {
            admin = true;
        } else {
            admin = false;
        }
        System.out.println("Admin: " + admin);
    }

    public void setColor(Color color) {
        this.color = color;
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