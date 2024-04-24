package com.example.cultureconnect.Projekt;

import com.example.cultureconnect.Person.Person;

import java.util.Date;
import java.util.List;

public class Projekt {
    private String titel;
    private List<Person> participants;
    private String description;
    private Person projectCreator;
    private Date startDate;
    private Date endDate;

    //ressourcer - eks. billeder og dokumenter skal muligvis tilføjes
    //mile dates og event dates


    public Projekt(String titel, List<Person> participants, String description,
                   Person projectCreator, Date startDate, Date endDate) {
        this.titel = titel;
        this.participants = participants;
        this.description = description;
        this.projectCreator = projectCreator;
        this.startDate = startDate;
        this.endDate = endDate;
    }//konstruktør for projekt

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public List<Person> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Person> participants) {
        this.participants = participants;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getProjectCreator() {
        return projectCreator;
    }

    public void setProjectCreator(Person projectCreator) {
        this.projectCreator = projectCreator;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    //getters og setters
}
