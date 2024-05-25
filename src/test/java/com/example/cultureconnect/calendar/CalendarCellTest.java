package com.example.cultureconnect.calendar;

import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CalendarCellTest {

    private CalendarCell calendarCell;

    @BeforeEach
    public void setUp() {
        calendarCell = new CalendarCell();
    }

    @Test
    public void testConstructorInitializesFields() {
        // Assert
        assertEquals(Color.TRANSPARENT, calendarCell.getFill());
        assertEquals(20, calendarCell.getArcWidth());
        assertEquals(20, calendarCell.getArcHeight());

        assertTrue(calendarCell.getEffect() instanceof DropShadow);
        DropShadow dropShadow = (DropShadow) calendarCell.getEffect();
        assertEquals(5, dropShadow.getRadius());
        assertEquals(3, dropShadow.getOffsetX());
        assertEquals(3, dropShadow.getOffsetY());
        assertEquals(Color.BLACK, dropShadow.getColor());
    }

    @Test
    public void testArcWidth() {
        // Act
        calendarCell.setArcWidth(30);

        // Assert
        assertEquals(30, calendarCell.getArcWidth());
    }

    @Test
    public void testArcHeight() {
        // Act
        calendarCell.setArcHeight(30);

        // Assert
        assertEquals(30, calendarCell.getArcHeight());
    }

    @Test
    public void testDropShadowEffect() {
        // Arrange
        DropShadow dropShadow = (DropShadow) calendarCell.getEffect();

        // Act
        dropShadow.setRadius(10);
        dropShadow.setOffsetX(5);
        dropShadow.setOffsetY(5);
        dropShadow.setColor(Color.RED);

        // Assert
        assertEquals(10, dropShadow.getRadius());
        assertEquals(5, dropShadow.getOffsetX());
        assertEquals(5, dropShadow.getOffsetY());
        assertEquals(Color.RED, dropShadow.getColor());
    }
}
