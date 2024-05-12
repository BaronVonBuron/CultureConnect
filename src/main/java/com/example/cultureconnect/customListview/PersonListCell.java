package com.example.cultureconnect.customListview;

import com.example.cultureconnect.Person.Person;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class PersonListCell extends ListCell {

    private Person person;
    private boolean isExpanded = false;
    private Label emailLabel = new Label();
    private Label phoneLabel = new Label();
    private Label nameLabel = new Label();
    private VBox vbox = new VBox();
    private HBox hbox = new HBox();

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
        setGraphic(hbox);
        setPrefSize(200, 50);
        setStyle("-fx-background-color: transparent");
        setOnMouseClicked(event -> {
            if (isExpanded) {
                vbox.getChildren().removeAll(emailLabel, phoneLabel);
                setPrefSize(200, 50);
            } else {
                emailLabel.setText("Email: " + getEmail());
                phoneLabel.setText("Telefon: " + new String(getTlfNr()));
                vbox.getChildren().addAll(emailLabel, phoneLabel);
                setPrefSize(200, 100);
            }
            isExpanded = !isExpanded;
        });
        vbox.getChildren().add(hbox);
        setGraphic(vbox);

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