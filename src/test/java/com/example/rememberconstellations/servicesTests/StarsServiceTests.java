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

class StarsServiceTests {

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
        star.setPositionInConstellation("Alpha");

        // Mocks
        when(starsRepository.findStarById(1)).thenReturn(Optional.of(star));
        when(starsRepository.findStarById(999)).thenReturn(Optional.empty());
        when(starsRepository.existsById(1)).thenReturn(true);
        when(starsRepository.existsById(999)).thenReturn(false);
        when(starsRepository.save(any(Star.class))).thenReturn(star);
        doNothing().when(starsRepository).deleteById(1);
    }

    /* CREATE */

    @Test
    void testCreateStar() {
        Star createdStar = starsService.createStar(star);

        assertThat(createdStar).isNotNull();
        assertThat(createdStar.getName()).isEqualTo("Betelgeuse");
        verify(starsRepository, times(1)).save(star);
    }

    /* READ */

    @Test
    void testGetStarById() {
        Optional<Star> foundStar = starsService.getStarById(1);

        assertThat(foundStar).isPresent();
        assertThat(foundStar.get().getName()).isEqualTo("Betelgeuse");
    }

    @Test
    void testGetStarByIdShouldReturnEmptyWhenNotFound() {
        Optional<Star> foundStar = starsService.getStarById(999);

        assertThat(foundStar).isNotPresent();
    }

    @Test
    void testGetStarsByCriteriaWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        when(starsRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(star), pageable, 1));

        List<Star> stars = starsService.getStarsByCriteria("Betelgeuse", null, null, null, null,
                null, null, null, null,
                null, pageable);

        assertThat(stars).isNotEmpty();
        assertThat(stars.get(0).getName()).isEqualTo("Betelgeuse");
    }

    @Test
    void testGetStarsByCriteriaWithoutPageable() {
        when(starsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(star));

        List<Star> stars = starsService.getStarsByCriteria("Betelgeuse", null, null, null, null,
                null, null, null, null,
                null, null);

        assertThat(stars).isNotEmpty();
        assertThat(stars.get(0).getName()).isEqualTo("Betelgeuse");
    }

    /* UPDATE */

    @Test
    void testUpdateStar() {
        Star updatedStar = new Star();
        updatedStar.setId(1);
        updatedStar.setName("New Betelgeuse");

        when(starsRepository.existsById(1)).thenReturn(true);
        when(starsRepository.save(any(Star.class))).thenReturn(updatedStar);

        Optional<Star> result = starsService.updateStar(1, updatedStar);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("New Betelgeuse");
        verify(starsRepository, times(1)).save(updatedStar);
    }

    @Test
    void testUpdateStarShouldReturnEmptyWhenNotFound() {
        Star updatedStar = new Star();
        updatedStar.setId(1);
        updatedStar.setName("New Betelgeuse");

        when(starsRepository.existsById(999)).thenReturn(false);

        Optional<Star> result = starsService.updateStar(999, updatedStar);

        assertThat(result).isNotPresent();
        verify(starsRepository, times(0)).save(updatedStar);
    }

    /* DELETE */

    @Test
    void testDeleteStar() {
        when(starsRepository.existsById(1)).thenReturn(true);

        boolean isDeleted = starsService.deleteStar(1);

        assertThat(isDeleted).isTrue();
        verify(starsRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteStarShouldReturnFalseWhenNotFound() {
        when(starsRepository.existsById(999)).thenReturn(false);

        boolean isDeleted = starsService.deleteStar(999);

        assertThat(isDeleted).isFalse();
        verify(starsRepository, times(0)).deleteById(999);
    }
}
