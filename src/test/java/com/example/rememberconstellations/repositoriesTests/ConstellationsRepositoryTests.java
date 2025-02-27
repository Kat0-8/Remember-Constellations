package com.example.rememberconstellations.repositoriesTests;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
import com.example.rememberconstellations.services.ConstellationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConstellationsRepositoryTests {

    @Mock
    private ConstellationsRepository constellationsRepository;

    @InjectMocks
    private ConstellationsService constellationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private static final String SUPERGIANT = "Supergiant";

    private List<Constellation> createMockConstellations() {
        Star star1 = new Star("Betelgeuse", SUPERGIANT, 18.0, 900, 3500, 120000, "Alpha");
        Star star2 = new Star("Rigel", SUPERGIANT, 21.0, 1200, 17000, 120000, "Beta");

        List<Star> stars = Arrays.asList(star1, star2);

        Constellation constellation1 = new Constellation("Orion", "ORI", stars);
        Constellation constellation2 = new Constellation("Taurus", "TAU", stars);

        return Arrays.asList(constellation1, constellation2);
    }

    @Test
    void testGetConstellations() {
        List<Constellation> constellations = createMockConstellations();

        when(constellationsRepository.getAllConstellations()).thenReturn(constellations);

        List<Constellation> result = constellationsService.getConstellations();

        assertEquals(2, result.size());
        assertEquals("Orion", result.get(0).getName());
        assertEquals("Taurus", result.get(1).getName());
        verify(constellationsRepository, times(1)).getAllConstellations();
    }

    @Test
    void testGetConstellationByName() {
        String name = "Orion";

        List<Star> stars = Arrays.asList(
                new Star("Betelgeuse", SUPERGIANT, 18.0, 900, 3500, 120000, "Alpha"),
                new Star("Rigel", SUPERGIANT, 21.0, 1200, 17000, 120000, "Beta")
        );

        Constellation constellation = new Constellation(name, "ORI", stars);

        when(constellationsRepository.getConstellationByName(name)).thenReturn(constellation);

        Constellation result = constellationsService.getConstellationByName(name);

        assertNotNull(result);
        assertEquals(name, result.getName());
        assertEquals("ORI", result.getAbbreviation());
        assertEquals(2, result.getStars().size());  // Assert that the stars list has been correctly initialized
        assertEquals("Betelgeuse", result.getStars().get(0).getName());  // Check the star name
        verify(constellationsRepository, times(1)).getConstellationByName(name);
    }

    @Test
    void testGetConstellationByName_NotFound() {
        String name = "Unknown";

        when(constellationsRepository.getConstellationByName(name)).thenReturn(null);

        Constellation result = constellationsService.getConstellationByName(name);

        assertNull(result);
        verify(constellationsRepository, times(1)).getConstellationByName(name);
    }

    @Test
    void testGetConstellationByAbbreviation() {
        String abbreviation = "TAU";

        Star star1 = new Star("Aldebaran", "Red Giant", 2.0, 600, 4300, 400, "Alpha");
        Star star2 = new Star("Alcyone", "Multiple star", 6, 10, 13000, 2400, "Eta");
        List<Star> stars = Arrays.asList(star1, star2);

        Constellation constellation = new Constellation("Taurus", abbreviation, stars);

        when(constellationsRepository.getConstellationByAbbreviation(abbreviation)).thenReturn(constellation);

        Constellation result = constellationsService.getConstellationByAbbreviation(abbreviation);

        assertNotNull(result);
        assertEquals("Taurus", result.getName());
        assertEquals(abbreviation, result.getAbbreviation());
        assertEquals(2, result.getStars().size());
        assertEquals("Aldebaran", result.getStars().get(0).getName());
        assertEquals("Alcyone", result.getStars().get(1).getName());
        verify(constellationsRepository, times(1)).getConstellationByAbbreviation(abbreviation);
    }

    @Test
    void testGetConstellationByAbbreviation_NotFound() {
        String abbreviation = "XYZ";

        when(constellationsRepository.getConstellationByAbbreviation(abbreviation)).thenReturn(null);

        Constellation result = constellationsService.getConstellationByAbbreviation(abbreviation);

        assertNull(result);
        verify(constellationsRepository, times(1)).getConstellationByAbbreviation(abbreviation);
    }
}