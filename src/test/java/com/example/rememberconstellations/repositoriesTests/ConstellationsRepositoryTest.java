package com.example.rememberconstellations.repositoriesTests;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
import com.example.rememberconstellations.repositories.StarsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ConstellationsRepositoryTest {

    @Autowired
    private ConstellationsRepository constellationsRepository;

    @Autowired
    private StarsRepository starsRepository;

    private Constellation constellation;

    @BeforeEach
    void setUp() {
        constellation = new Constellation();
        constellation.setName("Orion");

        constellationsRepository.save(constellation);

        Star star1 = new Star();
        star1.setName("Betelgeuse");
        star1.setConstellation(constellation);

        Star star2 = new Star();
        star2.setName("Rigel");
        star2.setConstellation(constellation);

        starsRepository.save(star1);
        starsRepository.save(star2);
    }

    @Test
    void testFindByIdWithStars() {
        Optional<Constellation> fetchedConstellation = constellationsRepository.findById(constellation.getId());

        assertTrue(fetchedConstellation.isPresent());
        Constellation foundConstellation = fetchedConstellation.get();

        assertEquals("Orion", foundConstellation.getName());
        assertNotNull(foundConstellation.getStars());
        assertEquals(2, foundConstellation.getStars().size());
        assertTrue(foundConstellation.getStars().stream().anyMatch(star -> star.getName().equals("Betelgeuse")));
        assertTrue(foundConstellation.getStars().stream().anyMatch(star -> star.getName().equals("Rigel")));
    }

    @Test
    void testFindByIdWithNoStars() {

        Constellation emptyConstellation = new Constellation();
        emptyConstellation.setName("Empty Constellation");
        constellationsRepository.save(emptyConstellation);

        Optional<Constellation> fetchedConstellation = constellationsRepository.findById(emptyConstellation.getId());

        assertTrue(fetchedConstellation.isPresent());
        Constellation foundConstellation = fetchedConstellation.get();

        assertEquals("Empty Constellation", foundConstellation.getName());
        assertNotNull(foundConstellation.getStars());
        assertTrue(foundConstellation.getStars().isEmpty());
    }
}

