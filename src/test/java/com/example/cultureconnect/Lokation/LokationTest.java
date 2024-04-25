package com.example.cultureconnect.Lokation;

import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.awt.print.PrinterJob;
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
    private Lokation bib = new Lokation(name,description,blue,medArb);

    @Test
    @DisplayName("Alm.input, name get/set")
    void getSetName() {
        bib.setName("Tønders biblioteker");
        assertEquals("Tønders biblioteker", bib.getName());
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
        bib.setFarveKode(Color.RED);
        assertEquals(Color.RED,bib.getFarveKode());
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
}