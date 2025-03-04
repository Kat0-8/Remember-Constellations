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
        constellation.setName("Orion");

        star = new Star();
        star.setName("Betelgeuse");
        star.setConstellation(constellation);

        starsRepository.save(star);
    }

    @Test
    void testFindStarByIdShouldReturnStarWithConstellation() {
        Optional<Star> foundStar = starsRepository.findStarById(star.getId());

        assertThat(foundStar).isPresent();
        assertThat(foundStar.get().getName()).isEqualTo(star.getName());
        assertThat(foundStar.get().getConstellation()).isNotNull();
        assertThat(foundStar.get().getConstellation().getName()).isEqualTo(constellation.getName());
    }

    @Test
    void testFindStarByIdShouldReturnEmptyWhenStarNotFound() {
        Optional<Star> foundStar = starsRepository.findStarById(999);

        assertThat(foundStar).isNotPresent();
    }
}
