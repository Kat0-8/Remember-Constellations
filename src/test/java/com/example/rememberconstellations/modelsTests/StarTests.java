package com.example.rememberconstellations.modelsTests;

import com.example.rememberconstellations.models.Star;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class StarTests {
    private Star star;
    private static final String UNDEFINED = "UNDEFINED";
    private static final String TEST_NAME = "Test name";
    private static final String TEST_TYPE = "Test type";
    private static final double TEST_MASS = 1.0;
    private static final double TEST_RADIUS = 1.0;
    private static final double TEST_TEMPERATURE = 1.0;
    private static final double TEST_LUMINOSITY = 1.0;
    private static final String TEST_POSITION = "Test position";

    @BeforeEach
    void setUp() {
        star = new Star();
    }

    @Test
    void testGetters() {
        assertEquals(UNDEFINED, star.getName());
        assertEquals(UNDEFINED, star.getType());
        assertEquals(0.0, star.getMass());
        assertEquals(0.0, star.getRadius());
        assertEquals(0.0, star.getTemperature());
        assertEquals(0.0, star.getLuminosity());
        assertEquals(UNDEFINED, star.getPositionInConstellation());
    }

    @Test
    void testSetters() {
        star.setName("Test name");
        assertEquals("Test name", star.getName());

        star.setType("Test type");
        assertEquals("Test type", star.getType());

        star.setMass(100.5);
        assertEquals(100.5, star.getMass());

        star.setRadius(10.9);
        assertEquals(10.9, star.getRadius());

        star.setTemperature(40.4);
        assertEquals(40.4, star.getTemperature());

        star.setLuminosity(42.5);
        assertEquals(42.5, star.getLuminosity());

        star.setPositionInConstellation("Test position");
        assertEquals("Test position", star.getPositionInConstellation());
    }

    @Test
    void testConstructorWithParameters() {
        star = new Star(TEST_NAME, TEST_TYPE, TEST_MASS, TEST_RADIUS,
                TEST_TEMPERATURE, TEST_LUMINOSITY, TEST_POSITION);
        assertEquals(TEST_NAME, star.getName());
        assertEquals(TEST_TYPE, star.getType());
        assertEquals(TEST_MASS, star.getMass());
        assertEquals(TEST_RADIUS, star.getRadius());
        assertEquals(TEST_TEMPERATURE, star.getTemperature());
        assertEquals(TEST_LUMINOSITY, star.getLuminosity());
        assertEquals(TEST_POSITION, star.getPositionInConstellation());
    }

    @Test
    void testDefaultConstructor() {
        star = new Star();
        assertEquals(UNDEFINED, star.getName());
        assertEquals(UNDEFINED, star.getType());
        assertEquals(0.0, star.getMass());
        assertEquals(0.0, star.getRadius());
        assertEquals(0.0, star.getTemperature());
        assertEquals(0.0, star.getLuminosity());
        assertEquals(UNDEFINED, star.getPositionInConstellation());
    }
}
