package com.example.rememberconstellations.servicesTests;

import com.example.rememberconstellations.dto.ConstellationDto;
import com.example.rememberconstellations.dto.StarDto;
import com.example.rememberconstellations.mappers.ConstellationMapper;
import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.services.ConstellationsService;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class ConstellationsServiceTests {

    @Mock
    private ConstellationsRepository constellationsRepository;

    @Mock
    private ConstellationMapper constellationMapper;

    @InjectMocks
    private ConstellationsService constellationsService;

    private ConstellationDto constellationDto;
    private StarDto star1;
    private StarDto star2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Initialize stars
        star1 = new StarDto(1, "Alpha Centauri", null, null, null, null, null, null, null, null, 1);
        star2 = new StarDto(2, "Beta Centauri", null, null, null, null, null, null, null, null, 1);

        // Initialize constellation
        constellationDto = new ConstellationDto(1, "Centaurus", "Cen", "Family", "Region", List.of(star1, star2));

        // Mock repository behavior
        when(constellationsRepository.findById(1)).thenReturn(Optional.of(new Constellation()));
        when(constellationsRepository.existsById(1)).thenReturn(true);
        when(constellationsRepository.existsById(999)).thenReturn(false);
        when(constellationsRepository.save(any(Constellation.class))).thenReturn(new Constellation());

        // Mock mapper behavior
        when(constellationMapper.mapToDto(any(Constellation.class))).thenReturn(constellationDto);
        when(constellationMapper.mapToEntity(any(ConstellationDto.class))).thenReturn(new Constellation());
    }

    /* CREATE */

    @Test
    void testCreateConstellation() {
        when(constellationsService.createConstellation(any(ConstellationDto.class))).thenReturn(constellationDto);

        ConstellationDto createdConstellationDto = constellationsService.createConstellation(constellationDto);

        assertThat(createdConstellationDto).isNotNull();
        assertThat(createdConstellationDto.getName()).isEqualTo("Centaurus");
        verify(constellationsService, times(1)).createConstellation(any(ConstellationDto.class));
    }

    /* READ */

    @Test
    void testGetConstellationById() {
        when(constellationsService.getConstellationById(1)).thenReturn(Optional.of(constellationDto));

        Optional<ConstellationDto> foundConstellationDto = constellationsService.getConstellationById(1);

        assertThat(foundConstellationDto).isPresent();
        assertThat(foundConstellationDto.get().getName()).isEqualTo("Centaurus");
    }

    @Test
    void testGetConstellationByIdShouldReturnEmptyWhenNotFound() {
        when(constellationsService.getConstellationById(999)).thenReturn(Optional.empty());

        Optional<ConstellationDto> foundConstellationDto = constellationsService.getConstellationById(999);

        assertThat(foundConstellationDto).isNotPresent();
    }

    @Test
    void testGetConstellationsByCriteriaWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        when(constellationsService.getConstellationsByCriteria(any(), any(), any(), any(), eq(pageable)))
                .thenReturn(List.of(constellationDto));

        List<ConstellationDto> constellations = constellationsService.getConstellationsByCriteria("Centaurus", null, null, null, pageable);

        assertThat(constellations).isNotEmpty();
        assertThat(constellations.get(0).getName()).isEqualTo("Centaurus");
    }

    @Test
    void testGetConstellationsByCriteriaWithoutPageable() {
        when(constellationsService.getConstellationsByCriteria(any(), any(), any(), any(), isNull()))
                .thenReturn(List.of(constellationDto));

        List<ConstellationDto> constellations = constellationsService.getConstellationsByCriteria("Centaurus", null, null, null, null);

        assertThat(constellations).isNotEmpty();
        assertThat(constellations.get(0).getName()).isEqualTo("Centaurus");
    }

    /* UPDATE */

    @Test
    void testUpdateConstellation() {
        ConstellationDto updatedConstellationDto = new ConstellationDto(1, "New Centaurus", "Cen", "Family", "Region", List.of(star1, star2));

        when(constellationsService.updateConstellation(anyInt(), any(ConstellationDto.class)))
                .thenReturn(Optional.of(updatedConstellationDto));

        Optional<ConstellationDto> result = constellationsService.updateConstellation(1, updatedConstellationDto);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("New Centaurus");
        verify(constellationsService, times(1)).updateConstellation(anyInt(), any(ConstellationDto.class));
    }

    @Test
    void testUpdateConstellationShouldReturnEmptyWhenNotFound() {
        ConstellationDto updatedConstellationDto = new ConstellationDto(1, "New Centaurus", "Cen", "Family", "Region", List.of(star1, star2));

        when(constellationsService.updateConstellation(anyInt(), any(ConstellationDto.class)))
                .thenReturn(Optional.empty());

        Optional<ConstellationDto> result = constellationsService.updateConstellation(999, updatedConstellationDto);

        assertThat(result).isNotPresent();
        verify(constellationsService, times(1)).updateConstellation(anyInt(), any(ConstellationDto.class));
    }

    /* DELETE */

    @Test
    void testDeleteConstellation() {
        when(constellationsService.deleteConstellation(anyInt())).thenReturn(true);

        boolean isDeleted = constellationsService.deleteConstellation(1);

        assertThat(isDeleted).isTrue();
        verify(constellationsService, times(1)).deleteConstellation(anyInt());
    }

    @Test
    void testDeleteConstellationShouldReturnFalseWhenNotFound() {
        when(constellationsService.deleteConstellation(anyInt())).thenReturn(false);

        boolean isDeleted = constellationsService.deleteConstellation(999);

        assertThat(isDeleted).isFalse();
        verify(constellationsService, times(1)).deleteConstellation(anyInt());
    }
}
