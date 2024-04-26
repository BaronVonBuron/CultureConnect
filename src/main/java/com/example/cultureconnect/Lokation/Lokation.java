package com.example.cultureconnect.Lokation;

import com.example.cultureconnect.Person.Person;
import javafx.scene.paint.Color;

import java.util.List;

public class Lokation {
    private String name;
    private String description;
    private Color farveKode;
    private List<Person> employees;

    //kontakt oplysninger som email og tlfnr skal muligvis tilføjes
    public Lokation(String name, String description, Color farveKode, List<Person> employees) {
        this.name = name;
        this.description = description;
        this.farveKode = farveKode;
        this.employees = employees;
    }//konstruktør for lokation

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Color getFarveKode() {
        return farveKode;
    }

    public void setFarveKode(Color farveKode) {
        this.farveKode = farveKode;
    }

    public List<Person> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Person> employees) {
        this.employees = employees;
    } //getters og setters
}
