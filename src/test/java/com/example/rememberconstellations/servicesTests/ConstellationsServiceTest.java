package com.example.rememberconstellations.servicesTests;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
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

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class ConstellationsServiceTest {

    @Mock
    private ConstellationsRepository constellationsRepository;

    @InjectMocks
    private ConstellationsService constellationsService;

    private Constellation constellation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        constellation = new Constellation();
        constellation.setId(1);
        constellation.setName("Orion");
        constellation.setAbbreviation("Ori");
        constellation.setFamily("Family1");
        constellation.setRegion("Northern");

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(constellation));
    }

    /* CREATE */

    @Test
    void testCreateConstellation() {
        when(constellationsRepository.save(any(Constellation.class))).thenReturn(constellation);

        Constellation createdConstellation = constellationsService.createConstellation(constellation);

        assertThat(createdConstellation).isNotNull();
        assertThat(createdConstellation.getName()).isEqualTo("Orion");
        verify(constellationsRepository, times(1)).save(constellation);
    }

    /* READ */

    @Test
    void testGetConstellationById() {
        Optional<Constellation> foundConstellation = constellationsService.getConstellationById(1);

        assertThat(foundConstellation).isPresent();
        assertThat(foundConstellation.get().getName()).isEqualTo("Orion");
    }

    @Test
    void testGetConstellationByIdShouldReturnEmptyWhenNotFound() {
        when(constellationsRepository.findById(999)).thenReturn(Optional.empty());

        Optional<Constellation> foundConstellation = constellationsService.getConstellationById(999);

        assertThat(foundConstellation).isNotPresent();
    }

    @Test
    void testGetConstellationsByCriteriaWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        when(constellationsRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(constellation), pageable, 1));

        List<Constellation> constellations = constellationsService.getConstellationsByCriteria("Orion",
                null, null, null, pageable);

        assertThat(constellations).isNotEmpty();
        assertThat(constellations.get(0).getName()).isEqualTo("Orion");
    }

    @Test
    void testGetConstellationsByCriteriaWithoutPageable() {
        when(constellationsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(constellation));

        List<Constellation> constellations = constellationsService.getConstellationsByCriteria("Orion",
                null, null, null, null);

        assertThat(constellations).isNotEmpty();
        assertThat(constellations.get(0).getName()).isEqualTo("Orion");
    }

    @Test
    void testGetConstellationsByCriteriaWithMultipleFilters() {
        Pageable pageable = PageRequest.of(0, 10);
        when(constellationsRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(constellation), pageable, 1));

        List<Constellation> constellations = constellationsService.getConstellationsByCriteria("Orion",
                "Ori", "Family1", "Northern", pageable);

        assertThat(constellations).isNotEmpty();
        assertThat(constellations.get(0).getName()).isEqualTo("Orion");
        assertThat(constellations.get(0).getAbbreviation()).isEqualTo("Ori");
        assertThat(constellations.get(0).getFamily()).isEqualTo("Family1");
        assertThat(constellations.get(0).getRegion()).isEqualTo("Northern");
    }


    /* UPDATE */

    @Test
    void testUpdateConstellation() {
        when(constellationsRepository.existsById(1)).thenReturn(true);
        when(constellationsRepository.save(any(Constellation.class))).thenReturn(constellation);

        Optional<Constellation> updatedConstellation = constellationsService.updateConstellation(1, constellation);

        assertThat(updatedConstellation).isPresent();
        assertThat(updatedConstellation.get().getName()).isEqualTo("Orion");
        verify(constellationsRepository, times(1)).save(constellation);
    }

    @Test
    void testUpdateConstellationShouldReturnEmptyWhenNotFound() {
        when(constellationsRepository.existsById(999)).thenReturn(false);

        Optional<Constellation> updatedConstellation = constellationsService.updateConstellation(999, constellation);

        assertThat(updatedConstellation).isNotPresent();
    }

    /* DELETE */

    @Test
    void testDeleteConstellation() {
        when(constellationsRepository.existsById(1)).thenReturn(true);

        boolean isDeleted = constellationsService.deleteConstellation(1);

        assertThat(isDeleted).isTrue();
        verify(constellationsRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteConstellationShouldReturnFalseWhenNotFound() {
        when(constellationsRepository.existsById(999)).thenReturn(false);

        boolean isDeleted = constellationsService.deleteConstellation(999);

        assertThat(isDeleted).isFalse();
        verify(constellationsRepository, times(0)).deleteById(999);
    }
}
