package com.example.cultureconnect.customListview;

import com.example.cultureconnect.Person.Person;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

public class PersonListCell extends ListCell {

    private Person person;

    public PersonListCell(Person person){
        super(); // Call the constructor of the superclass
        this.person = person;
        Circle frame = new Circle(30);
        //ImageView imageView = new ImageView(person.getPicture());
        Image image = new Image("https://www.w3schools.com/w3images/avatar2.png"); //placeholder if they have to picture
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(30);
        imageView.setFitWidth(30);
        //frame.setStroke(Color.valueOf(person.getLokation().getFarveKode())); //kan først bruges når de har en lokation
        frame.setStroke(javafx.scene.paint.Color.BLACK); //placeholder hvis der ikke er en lokation
        frame.setStrokeWidth(5);
        setText(person.getName());
        setGraphic(frame);
        setGraphic(imageView);
        setPrefSize(200, 50);
        setStyle("-fx-background-color: transparent");
        setOnMouseClicked(event -> {
            //TODO make the cell expand when clicked, to show the details of the Person.

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
