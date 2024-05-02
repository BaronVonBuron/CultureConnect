package com.example.cultureconnect.Person;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Projekt.Projekt;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    private String name = "Gitte";
    List<Projekt> mineProjekt = null;
    private String position = "bibliotekar";
    private Lokation lokation = null;
    private String område = null;
    private String mail = "gittebib@gamil.dk";
    private int tlf = 44444444;
    private Image image = null;
    private UUID id;
    private List<Person> persList = null;
    Date start = null;
    Date end = null;
    private Person gitte = new Person(name,mail,tlf,image,"cpr");

    @Test
    @DisplayName("Alm. input, name get/set")
    void getSetName() {
        gitte.setName("Gerald");
        assertEquals("Gerald",gitte.getName());
    }

    @Test
    @DisplayName("Invalid input til navn")
    void invalidName(){
        try {
            gitte.setName(null);
            fail("Personer skal have et navn");
        } catch (IllegalArgumentException e) {
            assertEquals("Personer skal have et navn", e.getMessage());
        }
    }

    @Test
    @DisplayName("Alm. input, projects get/set")
    void getSetMyProjects() {
        List<Projekt> myProj = new ArrayList<>();
        myProj.add(new Projekt("Proj",start,end,id));
        myProj.add(new Projekt("Proj2",start,end,id));

        gitte.setMyProjects(myProj);
        assertEquals(myProj,gitte.getMyProjects());
    }

    @Test
    @DisplayName("Alm. input, position get/set")
    void getSetPosition() {
        gitte.setPosition("Admin");
        assertEquals("Admin", gitte.getPosition());
    }

    @Test
    @DisplayName("Alm. input, lokation get/set")
    void getSetLokation() {
        Lokation arbejdsplads = new Lokation("Kulturskolen","et sted for alle",
                "green");

        gitte.setLokation(arbejdsplads);
        assertEquals(arbejdsplads,gitte.getLokation());
    }

    @Test
    @DisplayName("Alm. input, arbejdsområde get/set")
    void getSetArbejdsområde() {
        gitte.setArbejdsområde("undervisning");
        assertEquals("undervisning",gitte.getArbejdsområde());
    }

    @Test
    @DisplayName("Alm. input, email get/set")
    void getSetEmail() {
        gitte.setEmail("mymail@gmail.dk");
        assertEquals("mymail@gmail.dk",gitte.getEmail());
    }

    @Test
    @DisplayName("Alm. input, tlfNr get/set")
    void getSetTlfNr() {
        gitte.setTlfNr(55555555);
        assertEquals(55555555,gitte.getTlfNr());
    }

    @Test
    void getSetPicture() {
    }
}