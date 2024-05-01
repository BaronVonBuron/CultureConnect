package com.example.cultureconnect.Logic;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.databaseaccess.DAO;
import javafx.scene.image.Image;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Logic {

    private DAO dao;

    public Logic(){
        this.dao = new DAO();
    }


    public void testCreate(){
        Image image = new Image("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");
        Person person = new Person("Marianne", "panik@sd.dk", 12345678, image, "123456-1234");

        Lokation lokation = new Lokation("Tønder Bibliotek", "Tønder Bibliotek", "ff0000");

        Date startDate = new Date();
        Date endDate = new Date();
        endDate.setTime(startDate.getTime() + 1000000000);//1000000000 ms = 11 dage
        Projekt projekt = new Projekt("TestProjekt", startDate, endDate, UUID.randomUUID());

        dao.createPerson(person);
        dao.createLokation(lokation);
        dao.createProjekt(projekt);
    }

    public List<Projekt> getProjects() {
        List<Projekt> projects = dao.readAllProjects();

        return projects;
    }
}
