package com.example.rememberconstellations.repositoriesTests;

import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.repositories.StarsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class StarsRepositoryTests {

    @Autowired
    private StarsRepository starsRepository;

    private Star star;
    private Constellation constellation;

    @BeforeEach
    void setUp() {
        constellation = new Constellation();
        constellation.setId(1);
        constellation.setName("Orion");
        constellation.setAbbreviation("Ori");
        constellation.setFamily("Family1");
        constellation.setRegion("Northern");

        star = new Star();
        star.setId(1);
        star.setName("Betelgeuse");
        star.setType("Supergiant");
        star.setMass(18.0);
        star.setRadius(950.0);
        star.setTemperature(3500.0);
        star.setLuminosity(100000.0);
        star.setRightAscension(5.0);
        star.setDeclination(7.0);
        star.setPositionInConstellation("Aldebaran");
        star.setConstellation(constellation);

        starsRepository.save(star);
    }

    @Test
    void testFindStarByIdShouldReturnStarWithConstellation() {
        Optional<Star> foundStar = starsRepository.findStarById(star.getId());

        assertThat(foundStar).isPresent();
        assertThat(foundStar.get().getName()).isEqualTo("Betelgeuse");
        assertThat(foundStar.get().getConstellation()).isNotNull();
        assertThat(foundStar.get().getConstellation().getName()).isEqualTo("Orion");
    }

    @Test
    void testFindStarByIdShouldReturnEmptyWhenStarNotFound() {
        Optional<Star> foundStar = starsRepository.findStarById(999);

        assertThat(foundStar).isNotPresent();
    }
}
