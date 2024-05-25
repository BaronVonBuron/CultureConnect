package com.example.cultureconnect.Projekt;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjektTest {

    private Projekt projekt;
    private UUID id;
    private String titel;
    private Date startDate;
    private Date endDate;
    private Date arrangementDato;

    @BeforeEach
    public void setUp() {
        id = UUID.randomUUID();
        titel = "Test Projekt";
        startDate = new Date();
        endDate = new Date(startDate.getTime() + 86400000); // +1 day
        arrangementDato = new Date();
        projekt = new Projekt(titel, startDate, arrangementDato, endDate, id);
    }

    @Test
    public void testConstructor() {
        assertEquals(id.toString(), projekt.getId());
        assertEquals(titel, projekt.getTitel());
        assertEquals(startDate, projekt.getStartDate());
        assertEquals(endDate, projekt.getEndDate());
        assertEquals(arrangementDato, projekt.getArrangementDato());
        assertTrue(projekt.getParticipants().isEmpty());
        assertTrue(projekt.getProjectCreator().isEmpty());
        assertTrue(projekt.getProjektAktiviteter().isEmpty());
    }

    @Test
    public void testSetAndGetArrangementDato() {
        Date newDate = new Date();
        projekt.setArrangementDato(newDate);
        assertEquals(newDate, projekt.getArrangementDato());
    }

    @Test
    public void testAddAktivitet() {
        ProjektAktivitet aktivitet = mock(ProjektAktivitet.class);
        projekt.addAktivitet(aktivitet);
        assertTrue(projekt.getProjektAktiviteter().contains(aktivitet));
    }

    @Test
    public void testSetAndGetAktiviteter() {
        String aktiviteter = "New activities";
        projekt.setAktiviteter(aktiviteter);
        assertEquals(aktiviteter, projekt.getAktiviteter());
    }

    @Test
    public void testGetColor_Default() {
        assertEquals("ffffff", projekt.getColor());
    }

    @Test
    public void testSetAndGetColor() {
        String color = "000000";
        projekt.setColor(color);
        assertEquals(color, projekt.getColor());
    }

    @Test
    public void testSetTitel_Null() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                projekt.setTitel(null);
            }
        });
    }

    @Test
    public void testSetAndGetParticipants() {
        List<Person> participants = new ArrayList<>();
        Person participant = mock(Person.class);
        participants.add(participant);
        projekt.setParticipants(participants);
        assertEquals(participants, projekt.getParticipants());
    }

    @Test
    public void testSetParticipants_Empty() {
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                projekt.setParticipants(new ArrayList<>());
            }
        });
    }

    @Test
    public void testSetAndGetDescription() {
        String description = "This is a test project.";
        projekt.setDescription(description);
        assertEquals(description, projekt.getDescription());
    }

    @Test
    public void testSetAndGetProjectCreator() {
        List<Person> creators = new ArrayList<>();
        Person creator = mock(Person.class);
        creators.add(creator);
        projekt.setProjectCreator(creators);
        assertEquals(creators, projekt.getProjectCreator());
    }

    @Test
    public void testSetAndGetLokation() {
        Lokation lokation = mock(Lokation.class);
        projekt.setLokation(lokation);
        assertEquals(lokation, projekt.getLokation());
    }

    @Test
    public void testSetAndGetStartDate() {
        Date newStartDate = new Date();
        projekt.setStartDate(newStartDate);
        assertEquals(newStartDate, projekt.getStartDate());
    }

    @Test
    public void testSetAndGetEndDate() {
        Date newEndDate = new Date(startDate.getTime() + 172800000); // +2 days
        projekt.setEndDate(newEndDate);
        assertEquals(newEndDate, projekt.getEndDate());
    }

    @Test
    public void testSetEndDate_BeforeStartDate() {
        Date invalidEndDate = new Date(startDate.getTime() - 86400000); // -1 day
        assertThrows(IllegalArgumentException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                projekt.setEndDate(invalidEndDate);
            }
        });
    }

    @Test
    public void testSetAndGetNotes() {
        String notes = "These are notes.";
        projekt.setNotes(notes);
        assertEquals(notes, projekt.getNotes());
    }
}
