package com.example.rememberconstellations.modelsTests;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

//import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class ConstellationTests {

    private static final String TEST_NAME = "Test name";
    private static final String TEST_ABBREVIATION = "Test abbreviation";
    private static final String UNDEFINED = "UNDEFINED";
    private Constellation constellation;

    @BeforeEach
    void setUp() {
        constellation = new Constellation();
    }

    @Test
    void testGetters() {
        assertEquals(UNDEFINED, constellation.getName());
        assertEquals(UNDEFINED, constellation.getAbbreviation());
        assertTrue(constellation.getStars().isEmpty());
    }

    @Test
    void testSetters() {
        constellation.setName(TEST_NAME);
        assertEquals(TEST_NAME, constellation.getName());

        constellation.setAbbreviation(TEST_ABBREVIATION);
        assertEquals(TEST_ABBREVIATION, constellation.getAbbreviation());

        List<Star> testStars = Arrays.asList(new Star(), new Star());
        constellation.setStars(testStars);
        assertEquals(testStars, constellation.getStars());
    }

    @Test
    void testConstructorWithParameters() {
        List<Star> testStars = Arrays.asList(new Star(), new Star());
        Constellation testConstellation = new Constellation(TEST_NAME, TEST_ABBREVIATION, testStars);

        assertEquals(TEST_NAME, testConstellation.getName());
        assertEquals(TEST_ABBREVIATION, testConstellation.getAbbreviation());
        assertEquals(testStars, testConstellation.getStars());
    }

    @Test
    void testDefaultConstructor() {
        constellation = new Constellation();
        assertEquals(UNDEFINED, constellation.getName());
        assertEquals(UNDEFINED, constellation.getAbbreviation());
        assertNotNull(constellation.getStars());
        assertTrue(constellation.getStars().isEmpty());
    }
}

