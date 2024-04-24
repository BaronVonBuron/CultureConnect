package com.example.cultureconnect.Projekt;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import javafx.scene.image.Image;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProjektTest {

    @Test
    void getSetTitel() {
        //test for title
        List<Person> l = null;
        Person pers = null;
        Lokation lok = null;
        Date d = null;
        Date de = null;
        Projekt p = new Projekt("Project1",l,"a new project",
                pers, lok, d,de);

        p.setTitel("New Project");
        assertEquals("New Project", p.getTitel());
    }

    @Test
    void getSetParticipants() {
        Person pers = null;
        Lokation lok = null;
        Date d = null;
        Date de = null;
        List<Projekt> pro = null;
        Lokation l = null;
        Image i = null;
        List<Person> lp = new ArrayList<>();
        lp.add(new Person("Lena",pro,"Bibliotekar",l,"bøger",
                "lenamail@gmail.dk",1111111,i));
        lp.add(new Person("Hans", pro, "Musiklæerer",l,"klaver",
                "hansmail@gmail.dk",22222222,i));
        Projekt p = new Projekt("Project1",lp,"a new project",
                pers,lok,d,de);

        p.setParticipants(lp);
        assertEquals(lp,p.getParticipants());
    }

    @Test
    void getSetDescription() {
    }

    @Test
    void getSetProjectCreator() {
    }

    @Test
    void getSetLokation(){

    }

    @Test
    void getSetStartDate() {
    }

    @Test
    void getSetEndDate() {
    }
}