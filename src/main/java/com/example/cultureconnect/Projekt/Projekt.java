package com.example.cultureconnect.Projekt;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;

import java.util.Date;
import java.util.List;

public class Projekt {
    private String titel;
    private List<Person> participants;
    private String description;
    private Person projectCreator;
    private Lokation lokation;
    private Date startDate;
    private Date endDate;

    //ressourcer - eks. billeder og dokumenter skal muligvis tilføjes
    //mile dates og event dates - lister til dem

    public Projekt(String titel, List<Person> participants, String description,
                   Person projectCreator, Lokation lokation,
                   Date startDate, Date endDate) {
        this.titel = titel;
        this.participants = participants;
        this.description = description;
        this.projectCreator = projectCreator;
        this.startDate = startDate;
        this.endDate = endDate;
        this.lokation = lokation;
    }//konstruktør for projekt

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        if(titel == null){
            throw new IllegalArgumentException("Projekter skal have en titel");
        }
        this.titel = titel;
    }

    public List<Person> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Person> participants) {
        if (participants.isEmpty()){
            throw new IllegalArgumentException("Der skal være deltagere i et projekt");
        }
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

    public Lokation getLokation() {
        return lokation;
    }

    public void setLokation(Lokation lokation) {
        this.lokation = lokation;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        if (endDate != null && startDate.after(endDate)){
            throw new IllegalArgumentException("Start dato for et projekt kan ikke være efter slut datoen");
        }
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        if (startDate != null && endDate.before(startDate)){
            throw new IllegalArgumentException("Slut dato for et projekt kan ikke være før start datoen");
        }
        this.endDate = endDate;
    }
    //getters og setters
}
