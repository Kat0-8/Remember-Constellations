package com.example.rememberconstellations.servicesTests;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
import com.example.rememberconstellations.repositories.StarsRepository;
import com.example.rememberconstellations.services.ConstellationsService;
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

class ConstellationsServiceTests {

    @Mock
    private ConstellationsRepository constellationsRepository;

    @Mock
    private StarsRepository starsRepository;

    @InjectMocks
    private ConstellationsService constellationsService;

    private Constellation constellation;
    private Star star1;
    private Star star2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize stars
        star1 = new Star();
        star1.setId(1);
        star1.setName("Alpha Centauri");

        star2 = new Star();
        star2.setId(2);
        star2.setName("Beta Centauri");

        // Initialize constellation
        constellation = new Constellation();
        constellation.setId(1);
        constellation.setName("Centaurus");
        constellation.setAbbreviation("Cen");
        constellation.setStars(List.of(star1, star2));

        // Mock repository behavior
        when(constellationsRepository.findById(1)).thenReturn(Optional.of(constellation));
        when(constellationsRepository.existsById(1)).thenReturn(true);
        when(constellationsRepository.existsById(999)).thenReturn(false);
        when(constellationsRepository.save(any(Constellation.class))).thenReturn(constellation);
        when(starsRepository.save(any(Star.class))).thenAnswer(invocation -> {
            Star starToSave = invocation.getArgument(0);
            if (starToSave.getId() == 0) {
                starToSave.setId(1);
            }
            return starToSave;
        });
        when(starsRepository.findById(1)).thenReturn(Optional.of(star1));
        when(starsRepository.findById(2)).thenReturn(Optional.of(star2));
        when(starsRepository.findById(999)).thenReturn(Optional.empty());
    }

    /* CREATE */

    @Test
    void testCreateConstellation() {
        Constellation createdConstellation = constellationsService.createConstellation(constellation);

        assertThat(createdConstellation).isNotNull();
        assertThat(createdConstellation.getName()).isEqualTo("Centaurus");
        verify(constellationsRepository, times(1)).save(constellation);
    }

    /* READ */

    @Test
    void testGetConstellationById() {
        Optional<Constellation> foundConstellation = constellationsService.getConstellationById(1);

        assertThat(foundConstellation).isPresent();
        assertThat(foundConstellation.get().getName()).isEqualTo("Centaurus");
    }

    @Test
    void testGetConstellationByIdShouldReturnEmptyWhenNotFound() {
        Optional<Constellation> foundConstellation = constellationsService.getConstellationById(999);

        assertThat(foundConstellation).isNotPresent();
    }

    @Test
    void testGetConstellationsByCriteriaWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        when(constellationsRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(constellation), pageable, 1));

        List<Constellation> constellations = constellationsService.getConstellationsByCriteria("Centaurus", null, null, null, pageable);

        assertThat(constellations).isNotEmpty();
        assertThat(constellations.get(0).getName()).isEqualTo("Centaurus");
    }

    @Test
    void testGetConstellationsByCriteriaWithoutPageable() {
        when(constellationsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(constellation));

        List<Constellation> constellations = constellationsService.getConstellationsByCriteria("Centaurus", null, null, null, null);

        assertThat(constellations).isNotEmpty();
        assertThat(constellations.get(0).getName()).isEqualTo("Centaurus");
    }

    /* UPDATE */

    @Test
    void testUpdateConstellation() {
        Constellation updatedConstellation = new Constellation();
        updatedConstellation.setId(1);
        updatedConstellation.setName("New Centaurus");

        when(constellationsRepository.existsById(1)).thenReturn(true);
        when(constellationsRepository.save(any(Constellation.class))).thenReturn(updatedConstellation);

        Optional<Constellation> result = constellationsService.updateConstellation(1, updatedConstellation);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("New Centaurus");
        verify(constellationsRepository, times(1)).save(updatedConstellation);
    }

    @Test
    void testUpdateConstellationShouldReturnEmptyWhenNotFound() {
        Constellation updatedConstellation = new Constellation();
        updatedConstellation.setId(1);
        updatedConstellation.setName("New Centaurus");

        when(constellationsRepository.existsById(999)).thenReturn(false);

        Optional<Constellation> result = constellationsService.updateConstellation(999, updatedConstellation);

        assertThat(result).isNotPresent();
        verify(constellationsRepository, times(0)).save(updatedConstellation);
    }

    /* DELETE */

    @Test
    void testDeleteConstellation() {
        when(constellationsRepository.existsById(1)).thenReturn(true);

        boolean isDeleted = constellationsService.deleteConstellation(1);

        assertThat(isDeleted).isTrue();
        verify(constellationsRepository, times(1)).deleteById(1);
        verify(starsRepository, times(2)).save(any(Star.class));
    }

    @Test
    void testDeleteConstellationShouldReturnFalseWhenNotFound() {
        when(constellationsRepository.existsById(999)).thenReturn(false);

        boolean isDeleted = constellationsService.deleteConstellation(999);

        assertThat(isDeleted).isFalse();
        verify(constellationsRepository, times(0)).deleteById(999);
    }
}
