package com.example.cultureconnect.calendar;

import javafx.scene.effect.DropShadow;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class CalendarCell extends Rectangle {
    public CalendarCell() {
        super();
        setFill(Color.TRANSPARENT); // Set the fill to transparent
        setArcWidth(20);
        setArcHeight(20);

        DropShadow dropShadow = new DropShadow();
        dropShadow.setRadius(5); // Set the radius of the shadow
        dropShadow.setOffsetX(3); // Set the horizontal offset of the shadow
        dropShadow.setOffsetY(3); // Set the vertical offset of the shadow
        dropShadow.setColor(Color.BLACK); // Set the color of the shadow

        // Apply drop shadow effect to the rectangle
        setEffect(dropShadow);
    }
}