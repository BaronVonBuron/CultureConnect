package com.example.cultureconnect.calendar;

import com.example.cultureconnect.Projekt.Projekt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ProjektCellTest {

    private Projekt projektMock;
    private ProjektCell projektCell;

    @BeforeEach
    public void setUp() {
        projektMock = Mockito.mock(Projekt.class);
        projektCell = new ProjektCell(5, "FF5733", projektMock);
    }

    @Test
    public void testConstructorInitializesFields() {
        // Assert
        assertEquals(5, projektCell.getLength());
        assertEquals("FF5733", projektCell.getColor());
        assertEquals(projektMock, projektCell.getProjekt());
    }

    @Test
    public void testGetProjekt() {
        // Act
        Projekt result = projektCell.getProjekt();

        // Assert
        assertEquals(projektMock, result);
    }

    @Test
    public void testGetLength() {
        // Act
        int length = projektCell.getLength();

        // Assert
        assertEquals(5, length);
    }

    @Test
    public void testSetLength() {
        // Act
        projektCell.setLength(10);

        // Assert
        assertEquals(10, projektCell.getLength());
    }

    @Test
    public void testGetColor() {
        // Act
        String color = projektCell.getColor();

        // Assert
        assertEquals("FF5733", color);
    }

    @Test
    public void testSetColor() {
        // Act
        projektCell.setColor("00FF00");

        // Assert
        assertEquals("00FF00", projektCell.getColor());
    }
}
