package com.example.cultureconnect.customListview;

import com.example.cultureconnect.Logic.Logic;
import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.controllers.CultureConnectController;
import com.example.cultureconnect.controllers.NewLocationDialogController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class LokationListCell extends ListCell {

    private Lokation lokation;
    private boolean isExpanded = false;
    private Label nameLabel = new Label();
    private Label descriptionLabel = new Label();
    private VBox vbox = new VBox();
    private HBox hbox = new HBox();
    private HBox hboxButtons = new HBox();
    private Button editButton = new Button("Rediger");
    private Button deleteButton = new Button("Slet");
    private Logic logic;
    private Boolean admin;

    public LokationListCell(Lokation lokation){
        super(); // Call the constructor of the superclass
        this.lokation = lokation;
        Circle circle = new Circle(30);
        circle.setFill(Color.valueOf(lokation.getFarveKode()));
        nameLabel.setText(lokation.getName());
        hbox.getChildren().addAll(circle, nameLabel);
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
                    vbox.getChildren().removeAll(descriptionLabel,hboxButtons);
                    setPrefSize(195, 50);
                } else {
                    vbox.getChildren().removeAll(descriptionLabel);
                    setPrefSize(195, 50);
                }
            } else {
                if (admin) {
                    descriptionLabel.setText(getDescription());
                    vbox.getChildren().addAll(descriptionLabel,hboxButtons);
                    setPrefSize(195, 100);
                } else {
                    descriptionLabel.setText(getDescription());
                    vbox.getChildren().addAll(descriptionLabel);
                    setPrefSize(195, 100);
                }
            }
            isExpanded = !isExpanded;
        });
        vbox.getChildren().add(hbox);
        setGraphic(vbox);

        deleteButton.setOnMouseClicked(event -> {
            Lokation lokationClicked = this.getLokation();
            if (Objects.equals(lokationClicked.getName(), "Ingen Lokation")) {
                return;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"", ButtonType.YES, ButtonType.NO);
            alert.setHeaderText("Er du sikker på at du vil slette " + lokationClicked.getName() + "?");
            alert.getButtonTypes().clear();
            ButtonType buttonType = new ButtonType("Ja", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonType1 = new ButtonType("Nej", ButtonBar.ButtonData.CANCEL_CLOSE);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/CultureConnectCSS.css").toExternalForm());
            dialogPane.getStyleClass().add("Alerts");
            alert.getButtonTypes().addAll(buttonType, buttonType1);
            alert.showAndWait();

            if (alert.getResult() == buttonType) {
                this.logic = Logic.getInstance();
                logic.deleteLokation(lokationClicked);
            }
        });

        editButton.setOnMouseClicked(event -> {
            Lokation lokationClicked = this.getLokation();
            if (Objects.equals(lokationClicked.getName(), "Ingen Lokation")) {
                return;
            }
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/cultureconnect/NewLocationDialog.fxml"));
                Stage editLokation = new Stage();
                editLokation.setTitle("Rediger lokation");
                AnchorPane editLayout = loader.load();
                Scene scene = new Scene(editLayout);
                editLokation.setScene(scene);
                editLokation.setResizable(false);

                // Get the controller instance from the FXMLLoader
                NewLocationDialogController lokationController = loader.getController();

                // Call a method to initialize the controller with the clicked location
                lokationController.editInfo(lokationClicked);

                editLokation.show();
            } catch (IOException e) {
                System.out.println("Can't open edit location window");
            }
        });


        this.setOnDragDetected(event -> {
            Dragboard db = this.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            content.putString(lokation.getName());
            db.setContent(content);
            event.consume();
        });
        this.setOnDragDone(event -> {
            // Check if the drag operation was successful
            if (event.getTransferMode() == TransferMode.MOVE) {
                // Remove the item from the ListView's items
                System.out.println("drag done lokation listcell");
            }

            event.consume();
        });
    }

    public void findAdmin() {
        this.logic = Logic.getInstance();
        String userOrAdminNumber = logic.isAdmin(CultureConnectController.currentUser.getEmail());
        System.out.println("User or Admin Number: " + userOrAdminNumber);
        if (Objects.equals(userOrAdminNumber, "1")) {
            admin = true;
        } else {
            admin = false;
        }
        System.out.println("Admin: " + admin);
    }

    public Lokation getLokation() {
        return lokation;
    }

    public String getName() {
        return lokation.getName();
    }

    public String getDescription() {
        return lokation.getDescription();
    }
}