package com.example.cultureconnect.Logic;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.databaseaccess.DAO;

import java.util.ArrayList;
import java.util.List;

public class Logic {

    private static Logic instance = null;

    private DAO dao;
    private List<Person> persons = new ArrayList<>();
    private List<Lokation> locations = new ArrayList<>();
    private List<Projekt> projects = new ArrayList<>();
    private boolean isUpdating = false;
    private Person currentUser;

    private Logic(){
        this.dao = new DAO();
    }

    public static Logic getInstance(){
        if (instance == null){
            instance = new Logic();
        }
        return instance;
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
            this.projects = dao.readAllProjects(persons, locations);
            this.isUpdating = false;
        });
        t.start();
    }

    public boolean getIsUpdating() {
        return this.isUpdating;
    }

    public boolean login(String brugernavn, String kodeord) {

        //make dao call to check if user exists and password is correct
        if (dao.checkLoginReturnUser(brugernavn, kodeord)){
            for (Person person : persons) {
                if (person.getEmail().equals(brugernavn)) {
                    currentUser = person;
                    break;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public Person getCurrentUser() {
        return currentUser;
    }


    public void createProject(Projekt nytProjekt) {
        this.projects.add(nytProjekt);
        dao.createProjekt(nytProjekt);
    }

  
    public void createLocation(Lokation lokation){
        dao.createLokation(lokation);

    }
}
