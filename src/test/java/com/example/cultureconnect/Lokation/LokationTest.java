package com.example.cultureconnect.Lokation;

import com.example.cultureconnect.Person.Person;
import javafx.embed.swing.JFXPanel;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class LokationTest {

    private Lokation lokation;
    private String name;
    private String description;
    private String farveKode;

    @BeforeEach
    public void setUp() {
        name = "Test Lokation";
        description = "This is a test lokation";
        farveKode = "ff0000";
        lokation = new Lokation(name, description, farveKode);
    }

    @Test
    public void testConstructor() {
        assertEquals(name, lokation.getName());
        assertEquals(description, lokation.getDescription());
        assertEquals(farveKode, lokation.getFarveKode());
    }

    @Test
    public void testSetName_Null() {
        assertThrows(IllegalArgumentException.class, () -> {
            lokation.setName(null);
        });
    }

    @Test
    public void testSetAndGetName() {
        String newName = "New Lokation Name";
        lokation.setName(newName);
        assertEquals(newName, lokation.getName());
    }

    @Test
    public void testSetDescription() {
        String newDescription = "New description";
        lokation.setDescription(newDescription);
        assertEquals(newDescription, lokation.getDescription());
    }

    @Test
    public void testSetFarveKode_Null() {
        assertThrows(IllegalArgumentException.class, () -> {
            lokation.setFarveKode(null);
        });
    }

    @Test
    public void testSetAndGetFarveKode() {
        String newFarveKode = "00ff00";
        lokation.setFarveKode(newFarveKode);
        assertEquals(newFarveKode, lokation.getFarveKode());
    }

    @Test
    public void testSetAndGetAnsvarligPerson() {
        Person ansvarligPerson = mock(Person.class);
        lokation.setAnsvarligPerson(ansvarligPerson);
        assertEquals(ansvarligPerson, lokation.getAnsvarligPerson());
    }

    @Test
    public void testSetEmployees_Empty() {
        assertThrows(IllegalArgumentException.class, () -> {
            lokation.setEmployees(new ArrayList<>());
        });
    }

    @Test
    public void testSetAndGetEmployees() {
        List<Person> employees = new ArrayList<>();
        Person employee = mock(Person.class);
        employees.add(employee);
        lokation.setEmployees(employees);
        assertEquals(employees, lokation.getEmployees());
    }

    @Test
    public void testToString() {
        assertEquals(name, lokation.toString());
    }
}
