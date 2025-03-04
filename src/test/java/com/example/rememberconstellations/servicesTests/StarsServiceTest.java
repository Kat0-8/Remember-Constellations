package com.example.rememberconstellations.servicesTests;

import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.StarsRepository;
import com.example.rememberconstellations.services.StarsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
        import static org.assertj.core.api.Assertions.assertThat;

class StarsServiceTest {

    @Mock
    private StarsRepository starsRepository;

    @InjectMocks
    private StarsService starsService;

    private Star star;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

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

        when(starsRepository.findStarById(1)).thenReturn(Optional.of(star));
    }

    @Test
    void testGetStarById() {
        Optional<Star> foundStar = starsService.getStarById(1);

        assertThat(foundStar).isPresent();
        assertThat(foundStar.get().getName()).isEqualTo("Betelgeuse");
    }

    @Test
    void testGetStarByIdShouldReturnEmptyWhenNotFound() {
        when(starsRepository.findStarById(999)).thenReturn(Optional.empty());

        Optional<Star> foundStar = starsService.getStarById(999);

        assertThat(foundStar).isNotPresent();
    }

    @Test
    void testGetStarsByCriteriaWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        when(starsRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(star), pageable, 1));

        List<Star> stars = starsService.getStarsByCriteria("Betelgeuse", null, null, null, null, null, null, null, null, null, pageable);

        assertThat(stars).isNotEmpty();
        assertThat(stars.get(0).getName()).isEqualTo("Betelgeuse");
    }

    @Test
    void testGetStarsByCriteriaWithoutPageable() {
        when(starsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(star));

        List<Star> stars = starsService.getStarsByCriteria("Betelgeuse", null, null, null, null, null, null, null, null, null, null);

        assertThat(stars).isNotEmpty();
        assertThat(stars.get(0).getName()).isEqualTo("Betelgeuse");
    }

    @Test
    void testGetStarsByCriteriaWithMultipleFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        when(starsRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(star), pageable, 1));

        List<Star> stars = starsService.getStarsByCriteria("Betelgeuse", "Supergiant", 10.0, 800.0, 3000.0, 50000.0, 4.0, 6.0, "Aldebaran", 1, pageable);

        assertThat(stars).isNotEmpty();
        assertThat(stars.get(0).getName()).isEqualTo("Betelgeuse");
        assertThat(stars.get(0).getType()).isEqualTo("Supergiant");
        assertThat(stars.get(0).getMass()).isGreaterThanOrEqualTo(10.0);
        assertThat(stars.get(0).getRadius()).isGreaterThanOrEqualTo(800.0);
        assertThat(stars.get(0).getTemperature()).isGreaterThanOrEqualTo(3000.0);
        assertThat(stars.get(0).getLuminosity()).isGreaterThanOrEqualTo(50000.0);
        assertThat(stars.get(0).getRightAscension()).isGreaterThanOrEqualTo(4.0);
        assertThat(stars.get(0).getDeclination()).isGreaterThanOrEqualTo(6.0);
        assertThat(stars.get(0).getPositionInConstellation()).isEqualTo("Aldebaran");
    }
}
