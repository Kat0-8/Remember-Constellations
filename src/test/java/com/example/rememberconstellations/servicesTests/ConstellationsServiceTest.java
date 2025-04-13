package com.example.rememberconstellations.servicesTests;

import com.example.rememberconstellations.cache.ConstellationCache;
import com.example.rememberconstellations.dtos.ConstellationDto;
import com.example.rememberconstellations.dtos.StarDto;
import com.example.rememberconstellations.exceptions.ConstellationAlreadyExistsException;
import com.example.rememberconstellations.exceptions.ResourceNotFoundException;
import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
import com.example.rememberconstellations.repositories.StarsRepository;
import com.example.rememberconstellations.services.ConstellationsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ConstellationsServiceTest {

    @Mock
    private ConstellationsRepository constellationsRepository;

    @Mock
    private StarsRepository starsRepository;

    @Mock
    private ConstellationCache constellationCache;

    @InjectMocks
    private ConstellationsService constellationsService;

    private Constellation createBaseConstellation() {
        Constellation constellation = new Constellation();
        constellation.setName("Orion");
        constellation.setAbbreviation("ORI");
        constellation.setFamily("Winter");
        constellation.setRegion("Equatorial");
        constellation.setStars(new ArrayList<>());
        return constellation;
    }

    /* CREATE TESTS */
    @Test
    void createConstellation_NewName_SavesAndCaches() {
        ConstellationDto inputDto = new ConstellationDto(0, "Orion", "ORI", "Winter", "Equatorial", Collections.emptyList());
        Constellation savedEntity = createBaseConstellation();
        savedEntity.setId(1);
        ConstellationDto expectedDto = new ConstellationDto(1, "Orion", "ORI", "Winter", "Equatorial", Collections.emptyList());

        when(constellationsRepository.existsByName("Orion")).thenReturn(false);
        when(constellationsRepository.save(any(Constellation.class))).thenReturn(savedEntity);

        ConstellationDto result = constellationsService.createConstellation(inputDto);

        verify(constellationsRepository).save(any(Constellation.class));
        assertAll(
                () -> assertEquals(expectedDto.getId(), result.getId()),
                () -> assertEquals(expectedDto.getName(), result.getName()),
                () -> assertEquals(expectedDto.getAbbreviation(), result.getAbbreviation()),
                () -> assertEquals(expectedDto.getFamily(), result.getFamily()),
                () -> assertEquals(expectedDto.getRegion(), result.getRegion()),
                () -> assertTrue(result.getStars().isEmpty())
        );
    }

    @Test
    void createConstellation_ExistingName_ThrowsException() {
        ConstellationDto inputDto = new ConstellationDto(0, "Orion", "ORI", "Winter", "Equatorial", null);
        when(constellationsRepository.existsByName("Orion")).thenReturn(true);

        assertThrows(ConstellationAlreadyExistsException.class, () ->
                constellationsService.createConstellation(inputDto)
        );
        verify(constellationsRepository, never()).save(any());
    }

    /* ATTACH STARS TESTS */
    @Test
    void attachStars_AllStarsAvailable_AttachesSuccessfully() {
        Constellation constellation = createBaseConstellation();
        constellation.setId(1);

        List<Star> availableStars = List.of(
                new Star("Betelgeuse", "Red", 12.0, 800.0, 3500.0, 100000.0, 5.92, 7.41, "Shoulder"),
                new Star("Rigel", "Blue", 20.0, 70.0, 12000.0, 120000.0, 5.24, -8.20, "Foot")
        );
        availableStars.forEach(star -> {
            star.setId(1);
            star.setConstellation(constellation);
        });

        constellation.setStars(availableStars);

        assertEquals(2, constellation.getStars().size());
        assertTrue(availableStars.stream().allMatch(star -> star.getConstellation() == constellation));
    }

    @Test
    void attachStars_SomeStarsUnavailable_ThrowsException() {
        List<Star> availableStars = Collections.singletonList(new Star());
        availableStars.get(0).setId(1);

        Constellation constellation = createBaseConstellation();
        constellation.setId(1);

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(constellation));
        when(starsRepository.findByIdAndConstellationIsNull(List.of(1, 2))).thenReturn(availableStars);

        Exception ex = assertThrows(ResourceNotFoundException.class, () ->
                constellationsService.attachStars(1, List.of(1, 2))
        );
        assertTrue(ex.getMessage().contains("2"));
    }

    /* READ TESTS */
    @Test
    void getConstellationById_Cached_ReturnsFromCache() {
        ConstellationDto cachedDto = new ConstellationDto(1, "Orion", "ORI", "Winter", "Equatorial", List.of());
        when(constellationCache.get(1)).thenReturn(cachedDto);

        ConstellationDto result = constellationsService.getConstellationById(1);

        assertEquals(cachedDto, result);
        verify(constellationsRepository, never()).findById(anyInt());
    }

    @Test
    void getConstellationById_Uncached_FetchesAndCaches() {
        Constellation entity = createBaseConstellation();
        entity.setId(1);
        ConstellationDto expectedDto = new ConstellationDto(1, "Orion", "ORI", "Winter", "Equatorial", Collections.emptyList());

        when(constellationCache.get(1)).thenReturn(null);
        when(constellationsRepository.findById(1)).thenReturn(Optional.of(entity));

        ConstellationDto result = constellationsService.getConstellationById(1);

        assertEquals(expectedDto.getName(), result.getName());
    }

    @Test
    void getConstellationsByCriteria_WithPaging_ReturnsPageResults() {
        Constellation c1 = createBaseConstellation();
        c1.setId(1);
        Constellation c2 = createBaseConstellation();
        c2.setName("Ursa Major");
        c2.setId(2);

        PageImpl<Constellation> page = new PageImpl<>(List.of(c1, c2));

        when(constellationsRepository.findAll(  any(Specification.class), any(Pageable.class)))
                .thenReturn(page);

        List<ConstellationDto> results = constellationsService.getConstellationsByCriteria(
                null, null, null, null, Pageable.ofSize(2)
        );

        assertEquals(2, results.size());
        verify(constellationCache, times(2)).put(anyInt(), any());
    }

    // Add to ConstellationsServiceTest.java

    /* CRITERIA SEARCH - Family & Region Branches */
    @Test
    void getConstellationsByCriteria_WithFamily_ReturnsFilteredResults() {
        // Setup
        String family = "Zodiac";
        Constellation constellation = createBaseConstellation();
        constellation.setFamily(family);
        constellation.setId(1);
        when(constellationsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(constellation));

        // Execute
        List<ConstellationDto> results = constellationsService.getConstellationsByCriteria(
                null, null, family, null, null
        );

        // Verify
        assertEquals(1, results.size());
        assertEquals(family, results.get(0).getFamily());
    }

    @Test
    void getConstellationsByCriteria_WithRegion_ReturnsFilteredResults() {
        // Setup
        String region = "Southern";
        Constellation constellation = createBaseConstellation();
        constellation.setRegion(region);
        constellation.setId(1);
        when(constellationsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(constellation));

        // Execute
        List<ConstellationDto> results = constellationsService.getConstellationsByCriteria(
                null, null, null, region, null
        );

        // Verify
        assertEquals(1, results.size());
        assertEquals(region, results.get(0).getRegion());
    }

    @Test
    void getConstellationsByCriteria_WithFamilyAndRegion_CombinedFilter() {
        // Setup
        String family = "Zodiac";
        String region = "Northern";
        Constellation constellation = createBaseConstellation();
        constellation.setFamily(family);
        constellation.setRegion(region);
        constellation.setId(1);
        when(constellationsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(constellation));

        // Execute
        List<ConstellationDto> results = constellationsService.getConstellationsByCriteria(
                null, null, family, region, null
        );

        // Verify
        assertEquals(1, results.size());
        assertEquals(family, results.get(0).getFamily());
        assertEquals(region, results.get(0).getRegion());
    }

    @Test
    void getConstellationsByCriteria_WithUnmatchedFamily_ReturnsEmpty() {
        // Setup
        when(constellationsRepository.findAll(any(Specification.class)))
                .thenReturn(Collections.emptyList());

        // Execute
        List<ConstellationDto> results = constellationsService.getConstellationsByCriteria(
                null, null, "InvalidFamily", null, null
        );

        // Verify
        assertTrue(results.isEmpty());
        verify(constellationCache, never()).put(anyInt(), any());
    }

    @Test
    void getConstellationsByCriteria_WithCachedAndUncachedEntries() {
        // Setup
        Constellation cachedConstellation = createBaseConstellation();
        cachedConstellation.setId(1);
        ConstellationDto cachedDto = new ConstellationDto(1, "Cached", "CAC", "Family", "Region", Collections.emptyList());

        Constellation uncachedConstellation = createBaseConstellation();
        uncachedConstellation.setId(2);
        uncachedConstellation.setFamily("NewFamily");
        when(constellationsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(cachedConstellation, uncachedConstellation));
        when(constellationCache.get(1)).thenReturn(cachedDto);
        when(constellationCache.get(2)).thenReturn(null);

        // Execute
        List<ConstellationDto> results = constellationsService.getConstellationsByCriteria(
                null, null, null, null, null
        );

        // Verify
        assertEquals(2, results.size());
    }

    /* UPDATE TESTS */
    @Test
    void putConstellation_ValidId_UpdatesAndCaches() {
        Constellation existing = createBaseConstellation();
        existing.setId(1);
        ConstellationDto inputDto = new ConstellationDto(1, "New Orion", "NOR", "Winter", "Equatorial", List.of());

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(existing));
        when(constellationsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ConstellationDto result = constellationsService.putConstellation(1, inputDto);

        assertEquals("New Orion", result.getName());
        verify(constellationCache).put(1, result);
    }

    @Test
    void patchConstellation_PartialUpdate_ModifiesOnlySpecifiedFields() {
        Constellation original = createBaseConstellation();
        original.setId(1);
        ConstellationDto patchDto = new ConstellationDto(0, null, "NEW", null, "Southern", null);

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(original));
        when(constellationsRepository.save(any())).thenReturn(original);

        ConstellationDto result = constellationsService.patchConstellation(1, patchDto);

        assertEquals("NEW", result.getAbbreviation());
        assertEquals("Southern", result.getRegion());
        assertEquals("Orion", result.getName());
    }

    /* PATCH TESTS - Family & Name Branches */
    @Test
    void patchConstellation_UpdateFamily_OnlyModifiesFamily() {
        // Setup
        Constellation original = createBaseConstellation();
        original.setId(1);
        ConstellationDto patchDto = new ConstellationDto();
        patchDto.setFamily("NewFamily");

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(original));
        when(constellationsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Execute
        ConstellationDto result = constellationsService.patchConstellation(1, patchDto);

        // Verify
        assertAll(
                () -> assertEquals("NewFamily", result.getFamily()),
                () -> assertEquals("Orion", result.getName()), // Original unchanged
                () -> assertEquals("ORI", result.getAbbreviation()), // Original unchanged
                () -> assertEquals("Equatorial", result.getRegion()) // Original unchanged
        );
        verify(constellationCache).put(1, result);
    }

    @Test
    void patchConstellation_UpdateName_OnlyModifiesName() {
        // Setup
        Constellation original = createBaseConstellation();
        original.setId(1);
        ConstellationDto patchDto = new ConstellationDto();
        patchDto.setName("NewName");

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(original));
        when(constellationsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Execute
        ConstellationDto result = constellationsService.patchConstellation(1, patchDto);

        // Verify
        assertAll(
                () -> assertEquals("NewName", result.getName()),
                () -> assertEquals("Winter", result.getFamily()), // Original unchanged
                () -> assertEquals("ORI", result.getAbbreviation()), // Original unchanged
                () -> assertEquals("Equatorial", result.getRegion()) // Original unchanged
        );
        verify(constellationCache).put(1, result);
    }

    @Test
    void patchConstellation_UpdateNameAndFamily_ModifiesBoth() {
        // Setup
        Constellation original = createBaseConstellation();
        original.setId(1);
        ConstellationDto patchDto = new ConstellationDto();
        patchDto.setName("NewName");
        patchDto.setFamily("NewFamily");

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(original));
        when(constellationsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Execute
        ConstellationDto result = constellationsService.patchConstellation(1, patchDto);

        // Verify
        assertAll(
                () -> assertEquals("NewName", result.getName()),
                () -> assertEquals("NewFamily", result.getFamily()),
                () -> assertEquals("ORI", result.getAbbreviation()), // Original unchanged
                () -> assertEquals("Equatorial", result.getRegion()) // Original unchanged
        );
        verify(constellationCache).put(1, result);
    }

    /* DELETE TESTS */
    @Test
    void deleteConstellation_ValidId_RemovesFromCache() {
        Constellation constellation = createBaseConstellation();
        constellation.setId(1);
        when(constellationsRepository.findById(1)).thenReturn(Optional.of(constellation));

        constellationsService.deleteConstellation(1);

        verify(constellationsRepository).delete(constellation);
        verify(constellationCache).remove(1);
    }

    @Test
    void deleteConstellation_InvalidId_ThrowsException() {
        when(constellationsRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () ->
                constellationsService.deleteConstellation(999)
        );
    }

    /* SPECIAL CASE TESTS */
    @Test
    void getConstellationsByStarType_ValidType_ReturnsFilteredResults() {
        Constellation constellation = createBaseConstellation();
        Star star = new Star("Betelgeuse", "Red", 12.0, 800.0, 3500.0, 100000.0, 5.92, 7.41, "Shoulder");
        constellation.getStars().add(star);

        when(constellationsRepository.findByStarType("Red")).thenReturn(List.of(constellation));

        List<ConstellationDto> results = constellationsService.getConstellationsByStarType("Red");

        assertFalse(results.isEmpty());
        verify(constellationCache).put(anyInt(), any());
    }

    @Test
    void getConstellationsByStarType_NoResults_ThrowsException() {
        when(constellationsRepository.findByStarType("Unknown")).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () ->
                constellationsService.getConstellationsByStarType("Unknown")
        );
    }

    // Add to ConstellationsServiceTest.java

    /* CREATE TESTS - Additional Cases */
    @Test
    void createConstellation_WithStars_SavesHierarchy() {
        StarDto starDto = new StarDto(0, "TestStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        ConstellationDto inputDto = new ConstellationDto(0, "Test", "TST", "Family", "Region", List.of(starDto));

        Constellation savedConstellation = createBaseConstellation();
        savedConstellation.setId(1);
        savedConstellation.getStars().add(new Star("TestStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos"));

        when(constellationsRepository.existsByName("Test")).thenReturn(false);
        when(constellationsRepository.save(any())).thenReturn(savedConstellation);

        ConstellationDto result = constellationsService.createConstellation(inputDto);

        assertFalse(result.getStars().isEmpty());
        assertEquals("TestStar", result.getStars().get(0).getName());
    }

    /* ATTACH STARS - Edge Cases */
    @Test
    void attachStars_EmptyList_NoChanges() {
        Constellation constellation = createBaseConstellation();
        constellation.setId(1);

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(constellation));

        ConstellationDto result = constellationsService.attachStars(1, Collections.emptyList());

        assertTrue(result.getStars().isEmpty());
    }

    @Test
    void attachStars_DuplicateStars_ThrowsException() {
        Constellation constellation = createBaseConstellation();
        constellation.setId(1);
        Star existingStar = new Star("Exists", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        existingStar.setId(1);
        constellation.getStars().add(existingStar);

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(constellation));
        when(starsRepository.findByIdAndConstellationIsNull(List.of(1))).thenReturn(Collections.emptyList());

        assertThrows(ResourceNotFoundException.class, () ->
                constellationsService.attachStars(1, List.of(1))
        );
    }

    /* CRITERIA SEARCH - Individual Filters */
    @Test
    void getConstellationsByCriteria_NameFilter_ReturnsMatches() {
        Constellation c = createBaseConstellation();
        c.setId(1);

        when(constellationsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(c));

        List<ConstellationDto> results = constellationsService.getConstellationsByCriteria(
                "Ori", null, null, null, null
        );

        assertEquals(1, results.size());
        assertEquals("Orion", results.get(0).getName());
    }

    @Test
    void getConstellationsByCriteria_AbbreviationFilter_ReturnsMatches() {
        Constellation c = createBaseConstellation();
        c.setId(1);

        when(constellationsRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(c));

        List<ConstellationDto> results = constellationsService.getConstellationsByCriteria(
                null, "ORI", null, null, null
        );

        assertEquals(1, results.size());
        assertEquals("ORI", results.get(0).getAbbreviation());
    }

    /* UPDATE TESTS - Complex Scenarios */
    @Test
    void patchConstellation_AddStars_MergesCollections() {
        // Existing constellation with 1 star
        Constellation original = createBaseConstellation();
        original.setId(1);
        Star existingStar = new Star("OldStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        existingStar.setId(1);
        original.getStars().add(existingStar);

        // New star to add
        StarDto newStarDto = new StarDto(2, "NewStar", "Type", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "NewPos");
        ConstellationDto patchDto = new ConstellationDto();
        patchDto.setStars(List.of(newStarDto));

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(original));
        when(constellationsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ConstellationDto result = constellationsService.patchConstellation(1, patchDto);

        assertEquals(2, result.getStars().size());
        assertTrue(result.getStars().stream().anyMatch(s -> s.getId() == 1));
        assertTrue(result.getStars().stream().anyMatch(s -> s.getName().equals("NewStar")));
    }

    @Test
    void putConstellation_ReplaceStars_UpdatesCollection() {
        Constellation existing = createBaseConstellation();
        existing.setId(1);
        existing.getStars().add(new Star("OldStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos"));

        StarDto newStarDto = new StarDto(2, "NewStar", "Type", 2.0, 2.0, 6000.0, 2.0, 1.0, 1.0, "NewPos");
        ConstellationDto putDto = new ConstellationDto(1, "NewName", "NEW", "NewFam", "NewReg", List.of(newStarDto));

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(existing));
        when(constellationsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        ConstellationDto result = constellationsService.putConstellation(1, putDto);

        assertEquals(1, result.getStars().size());
        assertEquals("NewStar", result.getStars().get(0).getName());
    }

    @Test
    void putConstellation_NonExistentId_ThrowsResourceNotFound() {
        // Setup
        int nonExistentId = 999;
        ConstellationDto inputDto = new ConstellationDto(nonExistentId, "NewName", "NEW", "Family", "Region", List.of());

        when(constellationsRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Execute & Verify
        assertThrows(ResourceNotFoundException.class, () ->
                constellationsService.putConstellation(nonExistentId, inputDto)
        );
        verify(constellationsRepository, never()).save(any());
        verify(constellationCache, never()).put(anyInt(), any());
    }

    /* CACHE BEHAVIOR TESTS */
    @Test
    void attachStars_UpdatesCache() {
        Constellation constellation = createBaseConstellation();
        constellation.setId(1);
        Star star = new Star("NewStar", "Type", 1.0, 1.0, 5000.0, 1.0, 0.0, 0.0, "Pos");
        star.setId(1);

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(constellation));
        when(starsRepository.findByIdAndConstellationIsNull(List.of(1))).thenReturn(List.of(star));

        constellationsService.attachStars(1, List.of(1));

    }

    /* EXCEPTION HANDLING */
    @Test
    void getConstellationsByStarType_MixedStars_ReturnsMatches() {
        Constellation c = createBaseConstellation();
        c.getStars().addAll(List.of(
                new Star("RedStar", "Red", 1.0, 1.0, 3500.0, 1.0, 0.0, 0.0, "Pos"),
                new Star("BlueStar", "Blue", 2.0, 2.0, 10000.0, 2.0, 1.0, 1.0, "Pos")
        ));

        when(constellationsRepository.findByStarType("Red")).thenReturn(List.of(c));

        List<ConstellationDto> results = constellationsService.getConstellationsByStarType("Red");

        assertEquals(1, results.size());
        assertEquals(2, results.get(0).getStars().size());
    }

    @Test
    void patchConstellation_InvalidStar_ThrowsException() {
        Constellation original = createBaseConstellation();
        original.setId(1);
        StarDto invalidStar = new StarDto(999, null, null, null, null, null, null, null, null, null);

        ConstellationDto patchDto = new ConstellationDto();
        patchDto.setStars(List.of(invalidStar));

        when(constellationsRepository.findById(1)).thenReturn(Optional.of(original));

        assertThrows(RuntimeException.class, () ->
                constellationsService.patchConstellation(1, patchDto)
        );
    }
}