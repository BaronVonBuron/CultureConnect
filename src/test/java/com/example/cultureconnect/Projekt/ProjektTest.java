package com.example.cultureconnect.Projekt;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjektTest {
    private String title = "Project1";
    private List<Person> parList = null;
    private String description = "This is a project";
    private List<Projekt > projecList = null;
    private Person person = null;
    private Lokation lokation = null;
    private Image image = null;
    Date start = null;
    Date end = null;
    private Projekt p = new Projekt(title,parList,description, person, lokation, start,end);

    @Test
    @DisplayName("Alm. input, titel get/set")
    void getSetTitel() {
        p.setTitel("New Project");
        assertEquals("New Project", p.getTitel());
    }

    @Test
    @DisplayName("Invalid input til titel")
    void invalidTitle(){
        try {
            p.setTitel(null);
            fail("Projekter skal have en titel");
        } catch (IllegalArgumentException e) {
            assertEquals("Projekter skal have en titel", e.getMessage());
        }
    }

    @Test
    @DisplayName("Alm. input, participantsList get/set")
    void getSetParticipants() {
        List<Person> lp = new ArrayList<>();
        lp.add(new Person("Lena",projecList,"Bibliotekar",lokation,"bøger",
                "lenamail@gmail.dk",1111111,image));
        lp.add(new Person("Hans", projecList, "Musiklæerer",lokation,"klaver",
                "hansmail@gmail.dk",22222222,image));

        p.setParticipants(lp);
        assertEquals(lp,p.getParticipants());
    }

    @Test
    @DisplayName("Tom liste")
    void invalidParticipants(){
        List<Person> noParticipants = new ArrayList<>();

        try {
            p.setParticipants(noParticipants);
            fail("Der skal være deltagere i et projekt");
        } catch (IllegalArgumentException e) {
            assertEquals("Der skal være deltagere i et projekt", e.getMessage());
        }
    }

    @Test
    @DisplayName("Alm. input, description get/set")
    void getSetDescription() {
        p.setDescription("A fantastic project");
        assertEquals("A fantastic project",p.getDescription());
    }

    @Test
    @DisplayName("Alm. input, projectCreator get/set")
    void getSetProjectCreator() {
        Person gert = new Person("Gert",projecList,"maler",lokation,"rennovation",
                "gertyberty@gamil.com", 33333333, image);

        p.setProjectCreator(gert);
        assertEquals(gert,p.getProjectCreator());
    }

    @Test
    @DisplayName("Alm. input, lokation get/set")
    void getSetLokation(){
        Lokation nyFirma = new Lokation("Kultur",description, Color.BEIGE,parList);

        p.setLokation(nyFirma);
        assertEquals(nyFirma,p.getLokation());
    }

    @Test
    @DisplayName("Alm. input, startDate get/set")
    void getSetStartDate() {
        Date weStarted = new Date(11-11-2011);

        p.setStartDate(weStarted);
        assertEquals(weStarted,p.getStartDate());
    }

    @Test
    @DisplayName("Alm. input, endDate get/set")
    void getSetEndDate() {
        Date weFinished = new Date(11-11-2019);

        p.setEndDate(weFinished);
        assertEquals(weFinished,p.getEndDate());
    }

    @Test
    @DisplayName("Forkert start dato")
    void lateStartDate(){
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() - 10);

        try {
            p.setStartDate(startDate);
            p.setEndDate(endDate);
            fail("Slut dato for et projekt kan ikke være før start datoen");
        } catch (IllegalArgumentException e) {
            assertEquals("Slut dato for et projekt kan ikke være før start datoen", e.getMessage());
        }
    }
}