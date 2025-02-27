package com.example.rememberconstellations.modelsTests;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConstellationTests {

    private static final String TEST_NAME = "Test name";
    private static final String TEST_ABBREVIATION = "Test abbreviation";
    private static final String UNDEFINED = "UNDEFINED";
    private Constellation constellation;

    @BeforeEach
    void setUp() {
        constellation = new Constellation();
    }

    @Test
    public void testGetters() {
        assertEquals(UNDEFINED, constellation.getName());
        assertEquals(UNDEFINED, constellation.getAbbreviation());
        assertTrue(constellation.getStars().isEmpty());
    }

    @Test
    public void testSetters() {
        constellation.setName(TEST_NAME);
        assertEquals(TEST_NAME, constellation.getName());

        constellation.setAbbreviation(TEST_ABBREVIATION);
        assertEquals(TEST_ABBREVIATION, constellation.getAbbreviation());

        List<Star> testStars = Arrays.asList(new Star(), new Star());
        constellation.setStars(testStars);
        assertEquals(testStars, constellation.getStars());
    }

    @Test
    public void testConstructorWithParameters() {
        List<Star> testStars = Arrays.asList(new Star(), new Star());
        Constellation constellation = new Constellation(TEST_NAME, TEST_ABBREVIATION, testStars);

        assertEquals(TEST_NAME, constellation.getName());
        assertEquals(TEST_ABBREVIATION, constellation.getAbbreviation());
        assertEquals(testStars, constellation.getStars());
    }

    @Test
    public void testDefaultConstructor() {
        constellation = new Constellation(TEST_NAME, TEST_ABBREVIATION, new ArrayList<>());
        assertEquals(TEST_NAME, constellation.getName());
        assertEquals(TEST_ABBREVIATION, constellation.getAbbreviation());
        assertNotNull(constellation.getStars());
        assertTrue(constellation.getStars().isEmpty());
    }
}

