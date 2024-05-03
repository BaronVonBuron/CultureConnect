package com.example.cultureconnect.Logic;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.databaseaccess.DAO;

import java.util.ArrayList;
import java.util.List;

public class Logic {

    private DAO dao;
    private List<Person> persons = new ArrayList<>();
    private List<Lokation> locations = new ArrayList<>();
    private List<Projekt> projects = new ArrayList<>();
    private boolean isUpdating = false;

    public Logic(){
        this.dao = new DAO();
    }



    public List<Projekt> getProjects() {
        return projects;
    }

    public List<Person> getPersons() {
        return persons;
    }

    //make method to get all locations
    public List<Lokation> getLocations() {
        return locations;
    }

    public void updateLists() {
        Thread t = new Thread(() -> {
            this.isUpdating = true;
            this.persons = dao.readAllPersons();
            this.locations = dao.readAllLokations();
            this.projects = dao.readAllProjects();
            this.isUpdating = false;
        });
        t.start();
    }

    public boolean getIsUpdating() {
        return this.isUpdating;
    }
}
