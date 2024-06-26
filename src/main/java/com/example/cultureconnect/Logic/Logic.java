package com.example.cultureconnect.Logic;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.controllers.CultureConnectController;
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
    private CultureConnectController cultureConnectController;

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
        return this.projects;
    }

    public List<Person> getPersons() {
        return this.persons;
    }

    public List<Lokation> getLocations() {
        return locations;
    }

    public void updateLists() {
        Thread t = new Thread(() -> {
            this.isUpdating = true;
            this.locations = dao.readAllLokations();
            this.persons = dao.readAllPersons(locations);
            this.projects = dao.readAllProjects(persons, locations);
            this.isUpdating = false;
        });
        t.start();
    }

    public boolean getIsUpdating() {
        return this.isUpdating;
    }

    public boolean login(String brugernavn, String kodeord) {
        //kald til dao for at tjekke login
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
        this.locations.add(lokation);
        updateListsInController();
    }

    public void setAnsvarligForLocation(Lokation lok, Person ansvarlig){
        String lokation = lok.getName();
        String cpr = ansvarlig.getCPR();
        String stilling = ansvarlig.getPosition();

        dao.createOrUpdateMedarbejderInfo(lokation, cpr, stilling, true);
        updateListsInController();
    }

    public void updateAnsvarlig(Lokation lokation, Person ansvarlig){
        String lokationNavn = lokation.getName();
        String cpr = ansvarlig.getCPR();

        dao.updateAnsvarlig(lokationNavn, cpr, true);
        updateListsInController();
    }

    public void deletePerson(Person person){
        dao.deletePerson(person);
        for (Person person1 : this.persons) {
            if (person1.getCPR().equals(person.getCPR())) {
                this.persons.remove(person1);
                break;
            }
        }
        updateListsInController();
    }

    public void deleteLokation(Lokation lokation){
        dao.deleteLokation(lokation);
        for (Lokation location : this.locations) {
            if (location.getName().equals(lokation.getName())) {
                this.locations.remove(location);
                break;
            }
        }
        updateListsInController();
    }
    public void createUser(Person person){
        this.persons.add(person);
        dao.createPerson(person);
        updateListsInController();
    }

    public void updateLokation(Lokation lokation, String name){
        dao.updateLokation(lokation, name);
        for (Lokation location : this.locations) {
            if (location.getName().equals(lokation.getName())) {
                location.setName(name);
                location.setDescription(lokation.getDescription());
                location.setFarveKode(lokation.getFarveKode());
                break;
            }
        }
        updateListsInController();
    }

    public void deleteAnsvarlig(Lokation lokation, String cpr){
        dao.deleteAnsvarlig(lokation.getName(), cpr);
    }

    public String findBrugersLokation(String cpr){
        return dao.readLokationForPerson(cpr);
    }

    public String findBrugersStilling(String cpr){
        return dao.readStillingForPerson(cpr);
    }

    public Lokation findLokation(String lokationNavn){
        return dao.readLokation(lokationNavn);
    }

    public String findBrugerKodeord(String cpr){
        return dao.readKodeForPerson(cpr);
    }

    public void updateUser(Person nuværendePerson) {
        for (Person person : this.persons) {
            if (person.getCPR().equals(nuværendePerson.getCPR())) {
                person.setName(nuværendePerson.getName());
                person.setEmail(nuværendePerson.getEmail());
                person.setTlfNr(nuværendePerson.getTlfNr());
                person.setPicture(nuværendePerson.getPicture());
                person.setCPR(nuværendePerson.getCPR());
                person.setPosition(nuværendePerson.getPosition());
                person.setLokation(nuværendePerson.getLokation());
                break;
            }
        }
        dao.updatePerson(nuværendePerson);
        updateListsInController();
    }

    public void setMainWindowController(CultureConnectController cultureConnectController) {
        this.cultureConnectController = cultureConnectController;
    }
    public void updateListsInController(){
        cultureConnectController.updateList();
    }

    public void updateProjekt(Projekt projekt) {
        for (Projekt project : this.projects) {
            if(project.getId().equals(projekt.getId())){
                project.setProjektAktiviteter(projekt.getProjektAktiviteter());
                project.setDescription(projekt.getDescription());
                project.setParticipants(projekt.getParticipants());
                project.setProjectCreator(projekt.getProjectCreator());
                project.setTitel(projekt.getTitel());
                project.setLokation(projekt.getLokation());
                project.setEndDate(projekt.getEndDate());
                project.setStartDate(projekt.getStartDate());
                project.setColor(projekt.getColor());
                break;
            }
        }
        dao.updateProjekt(projekt);
        updateListsInController();
    }

    public void deleteProjekt(Projekt currentlySelectedProjekt) {
        for (Projekt project : this.projects) {
            if (project.getId().equals(currentlySelectedProjekt.getId())) {
                this.projects.remove(project);
                break;
            }
        }
        dao.deleteProjekt(currentlySelectedProjekt);
    }

    public String isAdmin(String email) {
        return dao.readAdminForPerson(email);
    }

    public Person findPerson(String email) {
        return dao.readPerson(email);
    }

    public List<String> findProjectOwner(String projectID){
        return dao.readParticipantsForProject(projectID);
    }

    public void setDao(DAO daoMock) {
        this.dao = daoMock;//for testing
    }
}
