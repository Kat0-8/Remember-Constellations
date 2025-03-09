package com.example.rememberconstellations.modelsTests;

import com.example.rememberconstellations.models.Star;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StarTests {

    private Star star;

    @BeforeEach
    void setUp() {
        star = new Star("Sirius", "Main Sequence", 2.1, 1.71, 9940, 25.4, 6.75, -16.72, "Center");
    }

    @Test
    void testGetters() {
        assertEquals("Sirius", star.getName());
        assertEquals("Main Sequence", star.getType());
        assertEquals(2.1, star.getMass());
        assertEquals(1.71, star.getRadius());
        assertEquals(9940, star.getTemperature());
        assertEquals(25.4, star.getLuminosity());
        assertEquals(6.75, star.getRightAscension());
        assertEquals(-16.72, star.getDeclination());
        assertEquals("Center", star.getPositionInConstellation());
    }

    @Test
    void testSetters() {
        star.setName("Alpha Centauri");
        assertEquals("Alpha Centauri", star.getName());

        star.setType("Red Giant");
        assertEquals("Red Giant", star.getType());

        star.setMass(1.2);
        assertEquals(1.2, star.getMass());

        star.setRadius(1.5);
        assertEquals(1.5, star.getRadius());

        star.setTemperature(6000.0);
        assertEquals(6000, star.getTemperature());

        star.setLuminosity(0.9);
        assertEquals(0.9, star.getLuminosity());

        star.setRightAscension(5.55);
        assertEquals(5.55, star.getRightAscension());

        star.setDeclination(-20.34);
        assertEquals(-20.34, star.getDeclination());

        star.setPositionInConstellation("Edge");
        assertEquals("Edge", star.getPositionInConstellation());
    }
}
