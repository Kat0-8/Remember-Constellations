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

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ConstellationsRepositoryTest {

    @Autowired
    private ConstellationsRepository constellationsRepository;

    @Autowired
    private StarsRepository starsRepository;

    private Constellation constellation;
    private Star star;

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
        constellationsRepository.save(constellation);
    }

    @Test
    void testFindByIdWithStars() {
        Optional<Constellation> foundConstellation = constellationsRepository.findById(constellation.getId());

        assertThat(foundConstellation).isPresent();
        assertThat(foundConstellation.get().getName()).isEqualTo("Orion");
        assertThat(foundConstellation.get().getStars()).isNotEmpty();
        assertThat(foundConstellation.get().getStars().get(0).getName()).isEqualTo("Betelgeuse");
    }

    @Test
    void testFindByIdWithNoStars() {
        Constellation emptyConstellation = new Constellation();
        emptyConstellation.setId(2);
        emptyConstellation.setName("Empty");
        emptyConstellation.setAbbreviation("Emp");
        emptyConstellation.setFamily("None");
        emptyConstellation.setRegion("Unknown");

        constellationsRepository.save(emptyConstellation);

        Optional<Constellation> foundConstellation = constellationsRepository.findById(emptyConstellation.getId());

        assertThat(foundConstellation).isPresent();
        assertThat(foundConstellation.get().getStars()).isEmpty();
    }
}
