package com.example.rememberconstellations.servicesTests;

import com.example.rememberconstellations.dto.StarDto;
import com.example.rememberconstellations.mappers.StarMapper;
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

    @Mock
    private StarMapper starMapper;

    @InjectMocks
    private StarsService starsService;

    private Star star;
    private StarDto starDto;

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

        starDto = new StarDto(1, "Betelgeuse", "Supergiant", 18.0, 950.0, 3500.0,
                100000.0, 5.0, 7.0, "Alpha");

        // Mocks
        when(starsRepository.findStarById(1)).thenReturn(Optional.of(star));
        when(starsRepository.findStarById(999)).thenReturn(Optional.empty());
        when(starsRepository.existsById(1)).thenReturn(true);
        when(starsRepository.existsById(999)).thenReturn(false);
        when(starsRepository.save(any(Star.class))).thenReturn(star);
        when(starMapper.mapToDto(any(Star.class))).thenReturn(starDto);
        when(starMapper.mapToEntity(any(StarDto.class))).thenReturn(star);
        doNothing().when(starsRepository).deleteById(1);
    }

    /* CREATE */

    @Test
    void testCreateStar() {
        StarDto createdStarDto = starsService.createStar(starDto);

        assertThat(createdStarDto).isNotNull();
        assertThat(createdStarDto.getName()).isEqualTo("Betelgeuse");
        verify(starsRepository, times(1)).save(star);
    }

    /* READ */

    @Test
    void testGetStarById() {
        Optional<StarDto> foundStarDto = starsService.getStarById(1);

        assertThat(foundStarDto).isPresent();
        assertThat(foundStarDto.get().getName()).isEqualTo("Betelgeuse");
    }

    @Test
    void testGetStarByIdShouldReturnEmptyWhenNotFound() {
        Optional<StarDto> foundStarDto = starsService.getStarById(999);

        assertThat(foundStarDto).isNotPresent();
    }

    @Test
    void testGetStarsByCriteriaWithPageable() {
        Pageable pageable = PageRequest.of(0, 10);
        when(starsRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(star), pageable, 1));

        List<StarDto> starDtos = starsService.getStarsByCriteria("Betelgeuse", null, null, null, null,
                null, null, null, null,
                null, pageable);

        assertThat(starDtos).isNotEmpty();
        assertThat(starDtos.get(0).getName()).isEqualTo("Betelgeuse");
    }

    @Test
    void testGetStarsByCriteriaWithoutPageable() {
        when(starsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(star));

        List<StarDto> starDtos = starsService.getStarsByCriteria("Betelgeuse", null, null, null, null,
                null, null, null, null,
                null, null);

        assertThat(starDtos).isNotEmpty();
        assertThat(starDtos.get(0).getName()).isEqualTo("Betelgeuse");
    }

    /* UPDATE */

    @Test
    void testUpdateStar() {
        StarDto updatedStarDto = new StarDto(1, "New Betelgeuse", "Supergiant", 18.0, 950.0, 3500.0,
                100000.0, 5.0, 7.0, "Alpha");

        when(starsRepository.existsById(1)).thenReturn(true);
        when(starsRepository.save(any(Star.class))).thenReturn(star);
        when(starMapper.mapToDto(any(Star.class))).thenReturn(updatedStarDto);
        when(starMapper.mapToEntity(any(StarDto.class))).thenReturn(star);

        Optional<StarDto> result = starsService.updateStar(1, updatedStarDto);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("New Betelgeuse");
        verify(starsRepository, times(1)).save(star);
    }

    @Test
    void testUpdateStarShouldReturnEmptyWhenNotFound() {
        StarDto updatedStarDto = new StarDto(1, "New Betelgeuse", "Supergiant", 18.0, 950.0, 3500.0,
                100000.0, 5.0, 7.0, "Alpha");

        when(starsRepository.existsById(999)).thenReturn(false);

        Optional<StarDto> result = starsService.updateStar(999, updatedStarDto);

        assertThat(result).isNotPresent();
        verify(starsRepository, times(0)).save(any(Star.class));
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
        verify(starsRepository, times(0)).deleteById(anyInt());
    }
}
