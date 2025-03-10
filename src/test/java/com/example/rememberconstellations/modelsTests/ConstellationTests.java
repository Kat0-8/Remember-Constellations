package com.example.rememberconstellations.modelsTests;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ConstellationTests {

    private Constellation constellation;

    @BeforeEach
    void setUp() {
        List<Star> stars = new ArrayList<>();
        constellation = new Constellation(1, "Orion", "ORI", "Orionidae", "Northern", stars);
    }

    @Test
    void testGetters() {
        assertEquals(1, constellation.getId());
        assertEquals("Orion", constellation.getName());
        assertEquals("ORI", constellation.getAbbreviation());
        assertEquals("Orionidae", constellation.getFamily());
        assertEquals("Northern", constellation.getRegion());
        assertNotNull(constellation.getStars());
        assertTrue(constellation.getStars().isEmpty());
    }

    @Test
    void testSetters() {
        constellation.setId(2);
        assertEquals(2, constellation.getId());

        constellation.setName("Andromeda");
        assertEquals("Andromeda", constellation.getName());

        constellation.setAbbreviation("AND");
        assertEquals("AND", constellation.getAbbreviation());

        constellation.setFamily("Andromedidae");
        assertEquals("Andromedidae", constellation.getFamily());

        constellation.setRegion("Southern");
        assertEquals("Southern", constellation.getRegion());

        List<Star> stars = new ArrayList<>();
        constellation.setStars(stars);
        assertEquals(stars, constellation.getStars());
    }
}
