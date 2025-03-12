package com.example.rememberconstellations.servicesTests;

import com.example.rememberconstellations.dto.ConstellationDto;
import com.example.rememberconstellations.dto.StarDto;
import com.example.rememberconstellations.mappers.ConstellationMapper;
import com.example.rememberconstellations.mappers.StarMapper;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class ConstellationsServiceTests {

    @Mock
    private ConstellationsRepository constellationsRepository;

    @Mock
    private ConstellationMapper constellationMapper;

    @Mock
    private StarMapper starMapper;

    @InjectMocks
    private ConstellationsService constellationsService;

    private Constellation constellation;
    private ConstellationDto constellationDto;
    private StarDto star1;
    private StarDto star2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize stars
        star1 = new StarDto(1, "Alpha Centauri", "Type A", 1.1, 1.2, 5800.0, 1.5, 14.660, -60.835, "Position A");
        star2 = new StarDto(2, "Beta Centauri", "Type B", 0.9, 0.8, 5200.0, 0.7, 13.467, -59.217, "Position B");

        constellation = new Constellation();
        constellation.setId(1);
        constellation.setName("Centaurus");
        constellation.setAbbreviation("Cen");
        constellation.setStars(List.of(starMapper.mapToEntity(star1), starMapper.mapToEntity(star2)));

        constellationDto = new ConstellationDto(1, "Centaurus", "Cen", "Family", "Region", List.of(star1, star2));

        // Mock repository behavior
        when(constellationsRepository.findById(1)).thenReturn(Optional.of(constellation));
        when(constellationsRepository.existsById(1)).thenReturn(true);
        when(constellationsRepository.existsById(999)).thenReturn(false);
        when(constellationsRepository.save(any(Constellation.class))).thenReturn(constellation);

        // Mock mapper behavior
        when(constellationMapper.mapToDto(any(Constellation.class))).thenReturn(constellationDto);
        when(constellationMapper.mapToEntity(any(ConstellationDto.class))).thenReturn(constellation);
    }

    /* CREATE */

    @Test
    void testCreateConstellation() {
        ConstellationDto createdConstellationDto = constellationsService.createConstellation(constellationDto);

        assertThat(createdConstellationDto).isNotNull();
        assertThat(createdConstellationDto.getName()).isEqualTo("Centaurus");
        verify(constellationsRepository, times(1)).save(constellation);
    }

    /* READ */

    @Test
    void testGetConstellationById() {
        Optional<ConstellationDto> foundConstellationDto = constellationsService.getConstellationById(1);

        assertThat(foundConstellationDto).isPresent();
        assertThat(foundConstellationDto.get().getName()).isEqualTo("Centaurus");
    }

    @Test
    void testGetConstellationByIdShouldReturnEmptyWhenNotFound() {
        Optional<ConstellationDto> foundConstellationDto = constellationsService.getConstellationById(999);

        assertThat(foundConstellationDto).isNotPresent();
    }

    @Test
    void testGetConstellationsByCriteriaWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        when(constellationsRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(constellation), pageable, 1));

        List<ConstellationDto> constellationDtos = constellationsService.getConstellationsByCriteria("Centaurus", null, null, null, pageable);

        assertThat(constellationDtos).isNotEmpty();
        assertThat(constellationDtos.get(0).getName()).isEqualTo("Centaurus");
    }

    @Test
    void testGetConstellationsByCriteriaWithoutPageable() {
        when(constellationsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(constellation));

        List<ConstellationDto> constellationDtos = constellationsService.getConstellationsByCriteria("Centaurus", null, null, null, null);

        assertThat(constellationDtos).isNotEmpty();
        assertThat(constellationDtos.get(0).getName()).isEqualTo("Centaurus");
    }

    /* UPDATE */

    @Test
    void testPutConstellation() {
        ConstellationDto updatedConstellationDto = new ConstellationDto(1, "New Centaurus", "Cen", "Family", "Region", List.of(star1, star2));

        when(constellationsRepository.existsById(1)).thenReturn(true);
        when(constellationsRepository.save(any(Constellation.class))).thenReturn(constellation);

        Optional<ConstellationDto> result = constellationsService.putConstellation(1, updatedConstellationDto);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("New Centaurus");
        verify(constellationsRepository, times(1)).save(constellation);
    }

    @Test
    void testPutConstellationShouldReturnEmptyWhenNotFound() {
        ConstellationDto updatedConstellationDto = new ConstellationDto(1, "New Centaurus", "Cen", "Family", "Region", List.of(star1, star2));

        when(constellationsRepository.existsById(999)).thenReturn(false);

        Optional<ConstellationDto> result = constellationsService.putConstellation(999, updatedConstellationDto);

        assertThat(result).isNotPresent();
        verify(constellationsRepository, times(0)).save(any(Constellation.class));
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
        verify(constellationsRepository, times(0)).deleteById(anyInt());
    }
}
