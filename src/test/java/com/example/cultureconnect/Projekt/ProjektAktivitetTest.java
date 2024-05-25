package com.example.cultureconnect.Projekt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class ProjektAktivitetTest {

    private ProjektAktivitet projektAktivitet;
    private LocalDate startDato;
    private LocalDate slutDato;
    private String aktivitet;

    @BeforeEach
    public void setUp() {
        startDato = LocalDate.of(2023, 5, 1);
        slutDato = LocalDate.of(2023, 5, 10);
        aktivitet = "Test Aktivitet";
        projektAktivitet = new ProjektAktivitet(startDato, slutDato, aktivitet);
    }

    @Test
    public void testConstructor() {
        assertEquals(startDato, projektAktivitet.getStartDato());
        assertEquals(slutDato, projektAktivitet.getSlutDato());
        assertEquals(aktivitet, projektAktivitet.getAktivitet());
    }

    @Test
    public void testGetStartDatoAsUtilDate() {
        Date expectedDate = Date.from(startDato.atStartOfDay(ZoneId.systemDefault()).toInstant());
        assertEquals(expectedDate, projektAktivitet.getStartDatoAsUtilDate());
    }

    @Test
    public void testGetEndDatoAsUtilDate() {
        Date expectedDate = Date.from(slutDato.atStartOfDay(ZoneId.systemDefault()).toInstant());
        assertEquals(expectedDate, projektAktivitet.getEndDatoAsUtilDate());
    }

    @Test
    public void testGetStartDatoAsSqlDate() {
        java.sql.Date expectedDate = java.sql.Date.valueOf(startDato);
        assertEquals(expectedDate, projektAktivitet.getStartDatoAsSqlDate());
    }

    @Test
    public void testGetEndDatoAsSqlDate() {
        java.sql.Date expectedDate = java.sql.Date.valueOf(slutDato);
        assertEquals(expectedDate, projektAktivitet.getEndDatoAsSqlDate());
    }

    @Test
    public void testSetAndGetStartDato() {
        LocalDate newStartDato = LocalDate.of(2023, 6, 1);
        projektAktivitet.setStartDato(newStartDato);
        assertEquals(newStartDato, projektAktivitet.getStartDato());
    }

    @Test
    public void testSetAndGetSlutDato() {
        LocalDate newSlutDato = LocalDate.of(2023, 6, 10);
        projektAktivitet.setSlutDato(newSlutDato);
        assertEquals(newSlutDato, projektAktivitet.getSlutDato());
    }

    @Test
    public void testSetAndGetAktivitet() {
        String newAktivitet = "New Aktivitet";
        projektAktivitet.setAktivitet(newAktivitet);
        assertEquals(newAktivitet, projektAktivitet.getAktivitet());
    }

    @Test
    public void testToString() {
        String expectedString = startDato.toString() + " | " + slutDato.toString() + " | " + aktivitet;
        assertEquals(expectedString, projektAktivitet.toString());
    }
}
