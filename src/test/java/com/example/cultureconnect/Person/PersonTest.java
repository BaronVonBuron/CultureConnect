package com.example.cultureconnect.Person;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Projekt.Projekt;
import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonTest {

    private Person person;
    private String name;
    private String email;
    private int tlfNr;
    private Image picture;
    private String CPR;

    @BeforeEach
    public void setUp() {
        // Required to initialize JavaFX toolkit
        new JFXPanel();

        name = "Test Person";
        email = "test@example.com";
        tlfNr = 12345678;
        CPR = "123456-7890";

        // Load the picture from the resources
        InputStream imageStream = getClass().getResourceAsStream("/images/avatar.png");
        picture = new Image(imageStream);
        person = new Person(name, email, tlfNr, picture, CPR);
    }

    @Test
    public void testConstructor() {
        assertEquals(name, person.getName());
        assertEquals(email, person.getEmail());
        assertEquals(tlfNr, person.getTlfNr());
        assertEquals(picture, person.getPicture());
        assertEquals(CPR, person.getCPR());
        assertFalse(person.isErAnsvarlig());
    }

    @Test
    public void testSetAndIsErAnsvarlig() {
        person.setErAnsvarlig(true);
        assertTrue(person.isErAnsvarlig());
    }

    @Test
    public void testSetName_Null() {
        assertThrows(IllegalArgumentException.class, () -> {
            person.setName(null);
        });
    }

    @Test
    public void testSetAndGetName() {
        String newName = "New Name";
        person.setName(newName);
        assertEquals(newName, person.getName());
    }

    @Test
    public void testSetAndGetCPR() {
        String newCPR = "098765-4321";
        person.setCPR(newCPR);
        assertEquals(newCPR, person.getCPR());
    }

    @Test
    public void testSetAndGetMyProjects() {
        List<Projekt> projects = new ArrayList<>();
        Projekt project = mock(Projekt.class);
        projects.add(project);
        person.setMyProjects(projects);
        assertEquals(projects, person.getMyProjects());
    }

    @Test
    public void testSetAndGetPosition() {
        String position = "Manager";
        person.setPosition(position);
        assertEquals(position, person.getPosition());
    }

    @Test
    public void testSetAndGetLokation() {
        Lokation lokation = mock(Lokation.class);
        person.setLokation(lokation);
        assertEquals(lokation, person.getLokation());
    }

    @Test
    public void testSetAndGetArbejdsområde() {
        String arbejdsområde = "IT";
        person.setArbejdsområde(arbejdsområde);
        assertEquals(arbejdsområde, person.getArbejdsområde());
    }

    @Test
    public void testSetAndGetEmail() {
        String newEmail = "new@example.com";
        person.setEmail(newEmail);
        assertEquals(newEmail, person.getEmail());
    }

    @Test
    public void testSetAndGetTlfNr() {
        int newTlfNr = 87654321;
        person.setTlfNr(newTlfNr);
        assertEquals(newTlfNr, person.getTlfNr());
    }

    @Test
    public void testSetAndGetKode() {
        String kode = "password";
        person.setKode(kode);
        assertEquals(kode, person.getKode());
    }

    @Test
    public void testSetAndGetPicture() {
        InputStream imageStream = getClass().getResourceAsStream("/images/avatar.png");
        Image newPicture = new Image(imageStream);
        person.setPicture(newPicture);
        assertEquals(newPicture, person.getPicture());
    }

    @Test
    public void testGetPictureAsByteArray() {
        byte[] byteArray = person.getPictureAsByteArray();
        assertNotNull(byteArray);
        assertTrue(byteArray.length > 0);
    }

    @Test
    public void testToString() {
        assertEquals(name, person.toString());
    }
}
