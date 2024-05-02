package com.example.cultureconnect.Lokation;

import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LokationTest {
    private String name = "Tønder bib";
    private String description = "Lån bøger";
    private Color blue = null;
    private List<Person> medArb = null;
    private List<Projekt> stuff = null;
    private Person per = null;
    private Lokation bib = new Lokation(name,description,"blue");

    @Test
    @DisplayName("Alm.input, name get/set")
    void getSetName() {
        bib.setName("Tønders biblioteker");
        assertEquals("Tønders biblioteker", bib.getName());
    }

    @Test
    @DisplayName("Invalid input til navn")
    void invalidName(){
        try {
            bib.setName(null);
            fail("Lokationer skal have navn");
        } catch (IllegalArgumentException e) {
            assertEquals("Lokationer skal have navn", e.getMessage());
        }
    }

    @Test
    @DisplayName("Alm. input, description get/set")
    void getSetDescription() {
        bib.setDescription("bog");
        assertEquals("bog",bib.getDescription());
    }

    @Test
    @DisplayName("Alm. input, farveKode get/set")
    void getSetFarveKode() {
        bib.setFarveKode("red");
        assertEquals("red",bib.getFarveKode());
    }

    @Test
    @DisplayName("Invalid farvekode")
    void invalidColor(){
        try {
            bib.setFarveKode(null);
            fail("Lokationer skal have en farve");
        } catch (IllegalArgumentException e) {
            assertEquals("Lokationer skal have en farve", e.getMessage());
        }
    }

    @Test
    @DisplayName("Alm. input, employees get/set")
    void getSetEmployees() {
        List<Person> folk = new ArrayList<>();
        folk.add(per);
        folk.add(per);

        bib.setEmployees(folk);
        assertEquals(folk, bib.getEmployees());
    }

    @Test
    @DisplayName("Tom liste")
    void invalidEmployees(){
        List<Person> noEmployees = new ArrayList<>();

        try {
            bib.setEmployees(noEmployees);
            fail("Der skal være medarbejdere på en lokation");
        } catch (IllegalArgumentException e) {
            assertEquals("Der skal være medarbejdere på en lokation", e.getMessage());
        }
    }
}