package com.example.cultureconnect.calendar;

import com.example.cultureconnect.Projekt.Projekt;

public class ProjektCell extends CalendarCell {
    //todo Make cells that will represent a Projekt as a rectangle, and place it according to the startdate and enddate in the gridpane.
    //todo Make the cells clickable, so that the user can see the details of the Projekt.
    //todo Make the cells color coded according to the creator of the Projekt. will get added later
    //If the projekt is longer than a week, the cell will be split into multiple cells, one for each week.
    //the first row of the gridpane resembles weeknumbers, so make a method that will check the weeknumber of the startdate and enddate, and place the cell accordingly.

    //this class should just take in a length of projekt, and a color, and then draw a rectangle with that length and color.

    private int length;
    private String color;
    private Projekt projekt;

    public ProjektCell(int length, String color, Projekt projekt){
        super(); // Call the constructor of the superclass
        this.length = length;
        this.color = color;
        this.projekt = projekt;
    }

    public Projekt getProjekt() {
        return projekt;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
