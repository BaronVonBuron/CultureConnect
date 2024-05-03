package com.example.cultureconnect.customListview;

import com.example.cultureconnect.Lokation.Lokation;
import javafx.scene.control.ListCell;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LokationListCell extends ListCell {
    //TODO make a cell that will hold a Lokation object, and display the name of the Lokation, and a colored circle according to the lokations color.
    //TODO make the cell clickable, and make it expand when clicked, to show the details of the Lokation.
    //TODO make the cell color coded according to the type of Lokation.

    //this class should take in a  Lokation object, and display the name of the Lokation, and a colored circle according to the lokations color.

    private Lokation lokation;

    public LokationListCell(Lokation lokation){
        super(); // Call the constructor of the superclass
        this.lokation = lokation;

        setText(lokation.getName());
        Circle circle = new Circle(30);
        circle.setFill(Color.valueOf(lokation.getFarveKode()));

        setGraphic(circle);
        setGraphicTextGap(20);
        setWrapText(true);
        setPrefSize(200, 50);
        setOnMouseClicked(event -> {
            //TODO make the cell expand when clicked, to show the details of the Lokation.

        });
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
