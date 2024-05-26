package com.example.cultureconnect.Lokation;

import com.example.cultureconnect.Person.Person;

import java.util.List;

public class Lokation {
    private String name;
    private String description;
    private String farveKode;
    private List<Person> employees;
    private Person ansvarligPerson;


    public Lokation(String name, String description, String farveKode) {
        this.name = name;
        this.description = description;
        this.farveKode = farveKode;
    }

    public String getName() {
        return name;
    }

    public Person getAnsvarligPerson() {
        return ansvarligPerson;
    }

    public void setAnsvarligPerson(Person ansvarligPerson) {
        this.ansvarligPerson = ansvarligPerson;
    }

    public void setName(String name) {
        if(name == null){
            throw new IllegalArgumentException("Lokationer skal have navn");
        }
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFarveKode() {
        return farveKode;
    }

    public void setFarveKode(String farveKode) {
        if (farveKode == null){
            throw new IllegalArgumentException("Lokationer skal have en farve");
        }
        this.farveKode = farveKode;
    }

    public List<Person> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Person> employees) {
        if (employees.isEmpty()){
            throw new IllegalArgumentException("Der skal være medarbejdere på en lokation");
        }
        this.employees = employees;
    }


    @Override
    public String toString() {
        return name;
    }
}
