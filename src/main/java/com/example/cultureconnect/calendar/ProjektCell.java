package com.example.cultureconnect.calendar;

import com.example.cultureconnect.Projekt.Projekt;

public class ProjektCell extends CalendarCell {

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
