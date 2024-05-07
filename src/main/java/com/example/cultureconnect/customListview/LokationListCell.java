package com.example.cultureconnect.customListview;

import com.example.cultureconnect.Lokation.Lokation;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class LokationListCell extends ListCell {

    private Lokation lokation;
    private boolean isExpanded = false;
    private Label nameLabel = new Label();
    private Label descriptionLabel = new Label();
    private VBox vbox = new VBox();
    private HBox hbox = new HBox();

    public LokationListCell(Lokation lokation){
        super(); // Call the constructor of the superclass
        this.lokation = lokation;
        Circle circle = new Circle(30);
        circle.setFill(Color.valueOf(lokation.getFarveKode()));
        nameLabel.setText(lokation.getName());
        hbox.getChildren().addAll(circle, nameLabel);
        hbox.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        hbox.setSpacing(5);
        setGraphic(hbox);
        setPrefSize(200, 50);
        setOnMouseClicked(event -> {
            if (isExpanded) {
                vbox.getChildren().remove(descriptionLabel);
                setPrefSize(200, 50);
            } else {
                descriptionLabel.setText(getDescription());
                vbox.getChildren().addAll(descriptionLabel);
                setPrefSize(200, 100);
            }
            isExpanded = !isExpanded;
        });
        vbox.getChildren().add(hbox);
        setGraphic(vbox);


        this.setOnDragDetected(event -> {
            System.out.println("drag detected lokation listcell");
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