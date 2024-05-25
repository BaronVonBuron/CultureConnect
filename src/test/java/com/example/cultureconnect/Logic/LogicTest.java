package com.example.cultureconnect.Logic;

import com.example.cultureconnect.Lokation.Lokation;
import com.example.cultureconnect.Person.Person;
import com.example.cultureconnect.Projekt.Projekt;
import com.example.cultureconnect.controllers.CultureConnectController;
import com.example.cultureconnect.databaseaccess.DAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LogicTest {

    @Mock
    private DAO daoMock;

    @Mock
    private CultureConnectController controllerMock;

    @InjectMocks
    private Logic logic;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        logic = Logic.getInstance();
        logic.setMainWindowController(controllerMock);
        // Ensure DAO mock is used
        logic.setDao(daoMock);
    }

    @Test
    public void testSingletonInstance() {
        Logic anotherInstance = Logic.getInstance();
        assertSame(logic, anotherInstance);
    }

    @Test
    public void testGetLocations() {
        List<Lokation> locations = new ArrayList<>();
        when(daoMock.readAllLokations()).thenReturn(locations);

        logic.updateLists();

        assertEquals(locations, logic.getLocations());
    }

    @Test
    public void testLogin_Success() {
        String username = "test@example.com";
        String password = "password";
        Person person = mock(Person.class);
        when(person.getEmail()).thenReturn(username);

        when(daoMock.checkLoginReturnUser(username, password)).thenReturn(true);
        logic.getPersons().add(person);

        assertTrue(logic.login(username, password));
        assertEquals(person, logic.getCurrentUser());
    }


    @Test
    public void testCreateProject() {
        Projekt projekt = mock(Projekt.class);

        logic.createProject(projekt);

        verify(daoMock).createProjekt(projekt);
        assertTrue(logic.getProjects().contains(projekt));
    }

    @Test
    public void testCreateLocation() {
        Lokation lokation = mock(Lokation.class);

        logic.createLocation(lokation);

        verify(daoMock).createLokation(lokation);
        assertTrue(logic.getLocations().contains(lokation));
        verify(controllerMock).updateList();
    }

    @Test
    public void testSetAnsvarligForLocation() {
        Lokation lokation = mock(Lokation.class);
        Person ansvarlig = mock(Person.class);
        when(lokation.getName()).thenReturn("Test Lokation");
        when(ansvarlig.getCPR()).thenReturn("1234");
        when(ansvarlig.getPosition()).thenReturn("Manager");

        logic.setAnsvarligForLocation(lokation, ansvarlig);

        verify(daoMock).createMedarbejderInfo("Test Lokation", "1234", "Manager", true);
        verify(controllerMock).updateList();
    }

    @Test
    public void testUpdateAnsvarlig() {
        Lokation lokation = mock(Lokation.class);
        Person ansvarlig = mock(Person.class);
        when(lokation.getName()).thenReturn("Test Lokation");
        when(ansvarlig.getCPR()).thenReturn("1234");

        logic.updateAnsvarlig(lokation, ansvarlig);

        verify(daoMock).updateAnsvarlig("Test Lokation", "1234", true);
        verify(controllerMock).updateList();
    }

    @Test
    public void testDeletePerson() {
        Person person = mock(Person.class);
        when(person.getCPR()).thenReturn("1234");
        logic.getPersons().add(person);

        logic.deletePerson(person);

        verify(daoMock).deletePerson(person);
        assertFalse(logic.getPersons().contains(person));
        verify(controllerMock).updateList();
    }

    @Test
    public void testDeleteLokation() {
        Lokation lokation = mock(Lokation.class);
        when(lokation.getName()).thenReturn("Test Lokation");
        logic.getLocations().add(lokation);

        logic.deleteLokation(lokation);

        verify(daoMock).deleteLokation(lokation);
        assertFalse(logic.getLocations().contains(lokation));
        verify(controllerMock).updateList();
    }


    @Test
    public void testUpdateProjekt() {
        Projekt projekt = mock(Projekt.class);
        UUID id = UUID.randomUUID();
        when(projekt.getId()).thenReturn(String.valueOf(id));
        logic.getProjects().add(projekt);

        Projekt updatedProjekt = mock(Projekt.class);
        when(updatedProjekt.getId()).thenReturn(String.valueOf(id));

        logic.updateProjekt(updatedProjekt);

        verify(daoMock).updateProjekt(updatedProjekt);
        verify(controllerMock).updateList();
    }

    @Test
    public void testDeleteProjekt() {
        Projekt projekt = mock(Projekt.class);
        UUID id = UUID.randomUUID();
        when(projekt.getId()).thenReturn(String.valueOf(id));
        logic.getProjects().add(projekt);

        logic.deleteProjekt(projekt);

        verify(daoMock).deleteProjekt(projekt);
        assertFalse(logic.getProjects().contains(projekt));
    }


    @Test
    public void testCreateUser() {
        Person person = mock(Person.class);

        logic.createUser(person);

        verify(daoMock).createPerson(person);
        assertTrue(logic.getPersons().contains(person));
        verify(controllerMock).updateList();
    }

    @Test
    public void testDeleteAnsvarlig() {
        Lokation lokation = mock(Lokation.class);
        when(lokation.getName()).thenReturn("Test Lokation");

        logic.deleteAnsvarlig(lokation, "1234");

        verify(daoMock).deleteAnsvarlig("Test Lokation", "1234");
    }

    @Test
    public void testFindBrugersLokation() {
        when(daoMock.readLokationForPerson("1234")).thenReturn("Test Lokation");

        String result = logic.findBrugersLokation("1234");

        assertEquals("Test Lokation", result);
    }

    @Test
    public void testFindBrugersStilling() {
        when(daoMock.readStillingForPerson("1234")).thenReturn("Manager");

        String result = logic.findBrugersStilling("1234");

        assertEquals("Manager", result);
    }

    @Test
    public void testFindLokation() {
        Lokation lokation = mock(Lokation.class);
        when(daoMock.readLokation("Test Lokation")).thenReturn(lokation);

        Lokation result = logic.findLokation("Test Lokation");

        assertEquals(lokation, result);
    }

    @Test
    public void testFindPerson() {
        Person person = mock(Person.class);
        when(daoMock.readPerson("test@example.com")).thenReturn(person);

        Person result = logic.findPerson("test@example.com");

        assertEquals(person, result);
    }

    @Test
    public void testFindProjectOwner() {
        List<String> participants = new ArrayList<>();
        when(daoMock.readParticipantsForProject("1234")).thenReturn(participants);

        List<String> result = logic.findProjectOwner("1234");

        assertEquals(participants, result);
    }
}
