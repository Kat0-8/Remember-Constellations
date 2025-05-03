package com.example.rememberconstellations.servicesTests;

import com.example.rememberconstellations.cache.StarCache;
import com.example.rememberconstellations.dtos.StarDto;
import com.example.rememberconstellations.exceptions.ResourceNotFoundException;
import com.example.rememberconstellations.exceptions.StarAlreadyExistsException;
import com.example.rememberconstellations.mappers.StarMapper;
import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.StarsRepository;
import com.example.rememberconstellations.services.StarsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StarsServiceTest {

    @Mock
    private StarsRepository starsRepository;

    @Mock
    private StarMapper starMapper;

    @Mock
    private StarCache starCache;

    @InjectMocks
    private StarsService starsService;

    // CREATE tests
    @Test
    void createStar_NewName_SavesAndCaches() {
        StarDto inputDto = new StarDto(0, "Sun", "G-type", 1.0, 1.0, 5778.0, 1.0, 0.0, 0.0, "Center",null);
        Star entity = new Star("Sun", "G-type", 1.0, 1.0, 5778.0, 1.0, 0.0, 0.0, "Center");
        Star savedEntity = new Star("Sun", "G-type", 1.0, 1.0, 5778.0, 1.0, 0.0, 0.0, "Center");
        savedEntity.setId(1);
        StarDto savedDto = new StarDto(1, "Sun", "G-type", 1.0, 1.0, 5778.0, 1.0, 0.0, 0.0, "Center", null);

        when(starsRepository.existsByName("Sun")).thenReturn(false);
        when(starMapper.mapToEntity(inputDto)).thenReturn(entity);
        when(starsRepository.save(entity)).thenReturn(savedEntity);
        when(starMapper.mapToDto(savedEntity)).thenReturn(savedDto);

        StarDto result = starsService.createStar(inputDto);

        verify(starsRepository).existsByName("Sun");
        verify(starsRepository).save(entity);
        verify(starCache).put(1, savedDto);
        assertEquals(savedDto, result);
    }

    @Test
    void createStar_ExistingName_ThrowsException() {
        StarDto inputDto = new StarDto(0, "Sun", "G-type", 1.0, 1.0, 5778.0, 1.0, 0.0, 0.0, "Center", null);
        when(starsRepository.existsByName("Sun")).thenReturn(true);

        assertThrows(StarAlreadyExistsException.class, () -> starsService.createStar(inputDto));
        verify(starsRepository, never()).save(any());
        verify(starCache, never()).put(anyInt(), any());
    }

    @Test
    void createStars_AllNew_SavesAndCaches() {
        StarDto dto1 = new StarDto(0, "Star1", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null);
        StarDto dto2 = new StarDto(0, "Star2", "Type", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "Pos", null);
        List<StarDto> dtos = List.of(dto1, dto2);

        Star star1 = new Star("Star1", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        Star star2 = new Star("Star2", "Type", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "Pos");
        List<Star> stars = List.of(star1, star2);

        Star savedStar1 = new Star("Star1", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        savedStar1.setId(1);
        Star savedStar2 = new Star("Star2", "Type", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "Pos");
        savedStar2.setId(2);
        List<Star> savedStars = List.of(savedStar1, savedStar2);

        StarDto savedDto1 = new StarDto(1, "Star1", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null);
        StarDto savedDto2 = new StarDto(2, "Star2", "Type", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "Pos", null);
        List<StarDto> savedDtos = List.of(savedDto1, savedDto2);

        when(starsRepository.findExistingStarsNamesIn(List.of("Star1", "Star2"))).thenReturn(Collections.emptyList());
        when(starMapper.mapToEntity(dto1)).thenReturn(star1);
        when(starMapper.mapToEntity(dto2)).thenReturn(star2);
        when(starsRepository.saveAll(stars)).thenReturn(savedStars);
        when(starMapper.mapToDto(savedStar1)).thenReturn(savedDto1);
        when(starMapper.mapToDto(savedStar2)).thenReturn(savedDto2);

        List<StarDto> result = starsService.createStars(dtos);

        verify(starsRepository).saveAll(stars);
        verify(starCache).put(1, savedDto1);
        verify(starCache).put(2, savedDto2);
        assertEquals(savedDtos, result);
    }

    @Test
    void createStars_SomeExist_ThrowsException() {
        StarDto dto = new StarDto(0, "ExistingStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null);
        List<StarDto> dtos = List.of(dto);

        when(starsRepository.findExistingStarsNamesIn(List.of("ExistingStar"))).thenReturn(List.of("ExistingStar"));

        assertThrows(StarAlreadyExistsException.class, () -> starsService.createStars(dtos));
        verify(starsRepository, never()).saveAll(any());
        verify(starCache, never()).put(anyInt(), any());
    }

    // READ tests
    @Test
    void getStarById_InCache_ReturnsCached() {
        StarDto cachedDto = new StarDto(1, "Sun", "G-type", 1.0, 1.0, 5778.0, 1.0, 0.0, 0.0, "Center", null);
        when(starCache.get(1)).thenReturn(cachedDto);

        StarDto result = starsService.getStarById(1);

        assertEquals(cachedDto, result);
        verify(starsRepository, never()).findById(anyInt());
    }

    @Test
    void getStarById_NotInCache_FetchesAndCaches() {
        Star entity = new Star("Sun", "G-type", 1.0, 1.0, 5778.0, 1.0, 0.0, 0.0, "Center");
        entity.setId(1);
        StarDto dto = new StarDto(1, "Sun", "G-type", 1.0, 1.0, 5778.0, 1.0, 0.0, 0.0, "Center", null);

        when(starCache.get(1)).thenReturn(null);
        when(starsRepository.findById(1)).thenReturn(Optional.of(entity));
        when(starMapper.mapToDto(entity)).thenReturn(dto);

        StarDto result = starsService.getStarById(1);

        verify(starCache).put(1, dto);
        assertEquals(dto, result);
    }

    @Test
    void getStarById_NotFound_ThrowsException() {
        when(starCache.get(1)).thenReturn(null);
        when(starsRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> starsService.getStarById(1));
    }
    @Test
    void putStar_NonExistentId_ThrowsResourceNotFound() {
        StarDto inputDto = new StarDto(999, "NewStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null);

        when(starsRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                starsService.putStar(999, inputDto)
        );
        verify(starCache, never()).put(anyInt(), any());
    }

    @Test
    void patchStar_NonExistentId_ThrowsResourceNotFound() {
        StarDto patchDto = new StarDto();
        patchDto.setName("UpdatedName");

        when(starsRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                starsService.patchStar(999, patchDto)
        );
    }

    /* CRITERIA SEARCH TESTS */
    @Test
    void getStarsByCriteria_WithPagination_ReturnsPagedResults() {
        // Setup
        Pageable pageable = Pageable.ofSize(2).first();
        Star star1 = new Star("Star1", "Type1", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos1");
        star1.setId(1);
        Star star2 = new Star("Star2", "Type2", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "Pos2");
        star2.setId(2);

        when(starsRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(List.of(star1, star2)));
        when(starMapper.mapToDto(star1)).thenReturn(new StarDto(1, "Star1", "Type1", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos1", null));
        when(starMapper.mapToDto(star2)).thenReturn(new StarDto(2, "Star2", "Type2", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "Pos2", null));

        // Execute
        List<StarDto> result = starsService.getStarsByCriteria(
                "Star", null, null, null, null, null, null, null, null, null, pageable
        );

        // Verify
        assertEquals(2, result.size());
        verify(starCache, times(2)).put(anyInt(), any());
    }

    @Test
    void getStarsByCriteria_WithoutPagination_ReturnsAllResults() {
        // Setup
        Star star = new Star("Star", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        star.setId(1);

        when(starsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(new StarDto(1, "Star", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null));

        // Execute
        List<StarDto> result = starsService.getStarsByCriteria(
                "Star", null, null, null, null, null, null, null, null, null, null
        );

        // Verify
        assertEquals(1, result.size());
        verify(starCache).put(1, result.get(0));
    }

    /* CACHE VERIFICATION TESTS */
    @Test
    void getStarsByCriteria_UpdatesCacheForNewEntries() {
        // Setup
        Star star = new Star("NewStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        star.setId(1);
        StarDto starDto = new StarDto(1, "NewStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null);

        when(starsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(starDto);
        when(starCache.get(1)).thenReturn(null); // Not in cache initially

        // Execute
        List<StarDto> result = starsService.getStarsByCriteria(
                "NewStar", null, null, null, null, null, null, null, null, null, null
        );

        // Verify
        verify(starCache).put(1, starDto);
        assertEquals(1, result.size());
    }

    @Test
    void getStarsByCriteria_WithType_ReturnsFilteredResults() {
        String type = "Type1";
        Star star = new Star("Star1", type, 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        star.setId(1);
        StarDto dto = new StarDto(1, "Star1", type, 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(dto);

        List<StarDto> result = starsService.getStarsByCriteria(
                null, type, null, null, null, null, null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals(type, result.get(0).getType());
        verify(starCache).put(1, dto);
    }

    @Test
    void getStarsByCriteria_WithMass_ReturnsFilteredResults() {
        Double mass = 2.0;
        Star star = new Star("Star1", "Type1", mass, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        star.setId(1);
        StarDto dto = new StarDto(1, "Star1", "Type1", mass, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(dto);

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, mass, null, null, null, null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals(mass, result.get(0).getMass());
        verify(starCache).put(1, dto);
    }

    @Test
    void getStarsByCriteria_WithNameAndType_ReturnsFilteredResults() {
        String name = "Star1";
        String type = "Type1";
        Star star = new Star(name, type, 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        star.setId(1);
        StarDto dto = new StarDto(1, name, type, 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(dto);

        List<StarDto> result = starsService.getStarsByCriteria(
                name, type, null, null, null, null, null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals(name, result.get(0).getName());
        assertEquals(type, result.get(0).getType());
        verify(starCache).put(1, dto);
    }

    @Test
    void getStarsByCriteria_NoCriteria_ReturnsAllStars() {
        Star star1 = new Star("Star1", "Type1", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        star1.setId(1);
        Star star2 = new Star("Star2", "Type2", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "Pos");
        star2.setId(2);
        List<Star> stars = List.of(star1, star2);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(stars);
        when(starMapper.mapToDto(star1)).thenReturn(new StarDto(1, "Star1", "Type1", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null));
        when(starMapper.mapToDto(star2)).thenReturn(new StarDto(2, "Star2", "Type2", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "Pos", null));

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, null
        );

        assertEquals(2, result.size());
        verify(starCache, times(2)).put(anyInt(), any());
    }

    @Test
    void getStarsByCriteria_WithPagination_ReturnsEmptyList() {
        Pageable pageable = Pageable.ofSize(10).withPage(1);
        when(starsRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new org.springframework.data.domain.PageImpl<>(Collections.emptyList()));

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, pageable
        );

        assertTrue(result.isEmpty());
        verify(starCache, never()).put(anyInt(), any());
    }

    @Test
    void getStarsByCriteria_WithSomeCachedStars_UpdatesCacheForNewEntries() {
        Star cachedStar = new Star("CachedStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        cachedStar.setId(1);
        StarDto cachedDto = new StarDto(1, "CachedStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos", null);

        Star newStar = new Star("NewStar", "Type", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "Pos");
        newStar.setId(2);
        StarDto newDto = new StarDto(2, "NewStar", "Type", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "Pos", null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(cachedStar, newStar));
        when(starMapper.mapToDto(cachedStar)).thenReturn(cachedDto);
        when(starMapper.mapToDto(newStar)).thenReturn(newDto);
        when(starCache.get(1)).thenReturn(cachedDto);
        when(starCache.get(2)).thenReturn(null);

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, null
        );

        assertEquals(2, result.size());
        verify(starCache).put(2, newDto);
        verify(starCache, never()).put(1, cachedDto);
    }

    @Test
    void getStarsByCriteria_WithRadius_ReturnsFilteredResults() {
        Double radius = 2.0;
        Star star = new Star("Star1", "Type", 1.0, radius, 5000.0, 1.0, 0.0, 0.0, "Pos");
        star.setId(1);
        StarDto dto = new StarDto(1, "Star1", "Type", 1.0, radius, 5000.0, 1.0, 0.0, 0.0, "Pos", null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(dto);

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, null, radius, null, null, null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals(radius, result.get(0).getRadius());
        verify(starCache).put(1, dto);
    }

    @Test
    void getStarsByCriteria_WithTemperature_ReturnsFilteredResults() {
        Double temperature = 6000.0;
        Star star = new Star("Star1", "Type", 1.0, 1.0, temperature, 1.0, 0.0, 0.0, "Pos");
        star.setId(1);
        StarDto dto = new StarDto(1, "Star1", "Type", 1.0, 1.0, temperature, 1.0, 0.0, 0.0, "Pos", null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(dto);

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, null, null, temperature, null, null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals(temperature, result.get(0).getTemperature());
        verify(starCache).put(1, dto);
    }

    @Test
    void getStarsByCriteria_WithLuminosity_ReturnsFilteredResults() {
        Double luminosity = 2.0;
        Star star = new Star("Star1", "Type", 1.0, 1.0, 5000.0, luminosity, 0.0, 0.0, "Pos");
        star.setId(1);
        StarDto dto = new StarDto(1, "Star1", "Type", 1.0, 1.0, 5000.0, luminosity, 0.0, 0.0, "Pos", null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(dto);

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, null, null, null, luminosity, null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals(luminosity, result.get(0).getLuminosity());
        verify(starCache).put(1, dto);
    }

    @Test
    void getStarsByCriteria_WithRightAscension_ReturnsFilteredResults() {
        Double rightAscension = 5.0;
        Star star = new Star("Star1", "Type", 1.0, 1.0, 5000.0, 1.0, rightAscension, 0.0, "Pos");
        star.setId(1);
        StarDto dto = new StarDto(1, "Star1", "Type", 1.0, 1.0, 5000.0, 1.0, rightAscension, 0.0, "Pos", null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(dto);

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, null, null, null, null, rightAscension, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals(rightAscension, result.get(0).getRightAscension());
        verify(starCache).put(1, dto);
    }

    @Test
    void getStarsByCriteria_WithDeclination_ReturnsFilteredResults() {
        Double declination = 10.0;
        Star star = new Star("Star1", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, declination, "Pos");
        star.setId(1);
        StarDto dto = new StarDto(1, "Star1", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, declination, "Pos", null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(dto);

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, null, null, null, null, null, declination, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals(declination, result.get(0).getDeclination());
        verify(starCache).put(1, dto);
    }

    @Test
    void getStarsByCriteria_WithPositionInConstellation_ReturnsFilteredResults() {
        String position = "Head";
        Star star = new Star("Star1", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, position);
        star.setId(1);
        StarDto dto = new StarDto(1, "Star1", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, position, null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(dto);

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, null, null, null, null, null, null, position, null, null
        );

        assertEquals(1, result.size());
        assertEquals(position, result.get(0).getPositionInConstellation());
        verify(starCache).put(1, dto);
    }

    @Test
    void getStarsByCriteria_WithRadiusAndTemperature_ReturnsFilteredResults() {
        Double radius = 2.0;
        Double temperature = 6000.0;
        Star star = new Star("Star1", "Type", 1.0, radius, temperature, 1.0, 0.0, 0.0, "Pos");
        star.setId(1);
        StarDto dto = new StarDto(1, "Star1", "Type", 1.0, radius, temperature, 1.0, 0.0, 0.0, "Pos", null);

        when(starsRepository.findAll(any(Specification.class))).thenReturn(List.of(star));
        when(starMapper.mapToDto(star)).thenReturn(dto);

        List<StarDto> result = starsService.getStarsByCriteria(
                null, null, null, radius, temperature, null, null, null, null, null, null
        );

        assertEquals(1, result.size());
        assertEquals(radius, result.get(0).getRadius());
        assertEquals(temperature, result.get(0).getTemperature());
        verify(starCache).put(1, dto);
    }

    /* PARTIAL UPDATE TESTS */
    @Test
    void patchStar_UpdateSingleField_PreservesOtherValues() {
        // Setup
        Star existingStar = new Star("Original", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        existingStar.setId(1);
        StarDto patchDto = new StarDto();
        patchDto.setMass(2.0);

        when(starsRepository.findById(1)).thenReturn(Optional.of(existingStar));
        when(starsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(starMapper.mapToDto(any())).thenAnswer(inv -> {
            Star s = inv.getArgument(0);
            return new StarDto(s.getId(), s.getName(), s.getType(), s.getMass(),
                    s.getRadius(), s.getTemperature(), s.getLuminosity(),
                    s.getRightAscension(), s.getDeclination(), s.getPositionInConstellation(), null);
        });

        // Execute
        StarDto result = starsService.patchStar(1, patchDto);

        // Verify
        assertAll(
                () -> assertEquals(2.0, result.getMass()),
                () -> assertEquals("Original", result.getName()),
                () -> assertEquals(1.0, result.getRadius())
        );
    }

    @Test
    void patchStar_UpdateAllFields_UpdatesCorrectly() {
        // Setup
        Star existingStar = new Star("Old", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        existingStar.setId(1);
        StarDto patchDto = new StarDto();
        patchDto.setName("New");
        patchDto.setType("NewType");
        patchDto.setMass(2.0);
        patchDto.setRadius(2.0);
        patchDto.setTemperature(6000.0);
        patchDto.setLuminosity(2.0);
        patchDto.setRightAscension(1.0);
        patchDto.setDeclination(1.0);
        patchDto.setPositionInConstellation("NewPos");

        when(starsRepository.findById(1)).thenReturn(Optional.of(existingStar));
        when(starsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(starMapper.mapToDto(any())).thenAnswer(inv -> {
            Star s = inv.getArgument(0);
            return new StarDto(s.getId(), s.getName(), s.getType(), s.getMass(),
                    s.getRadius(), s.getTemperature(), s.getLuminosity(),
                    s.getRightAscension(), s.getDeclination(), s.getPositionInConstellation(), null);
        });

        // Execute
        StarDto result = starsService.patchStar(1, patchDto);

        // Verify
        assertAll(
                () -> assertEquals("New", result.getName()),
                () -> assertEquals("NewType", result.getType()),
                () -> assertEquals(2.0, result.getMass()),
                () -> assertEquals(2.0, result.getRadius()),
                () -> assertEquals(6000.0, result.getTemperature()),
                () -> assertEquals(2.0, result.getLuminosity()),
                () -> assertEquals(1.0, result.getRightAscension()),
                () -> assertEquals(1.0, result.getDeclination()),
                () -> assertEquals("NewPos", result.getPositionInConstellation())
        );
    }

    /* DELETE VERIFICATION TEST */
    @Test
    void deleteStar_VerifyCacheRemoval() {
        // Setup
        Star star = new Star("ToDelete", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        star.setId(1);
        when(starsRepository.findById(1)).thenReturn(Optional.of(star));

        // Execute
        starsService.deleteStar(1);

        // Verify
        verify(starCache).remove(1);
        verify(starsRepository).delete(star);
    }
}

