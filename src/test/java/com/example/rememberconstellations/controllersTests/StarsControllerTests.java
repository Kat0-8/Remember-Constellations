package com.example.rememberconstellations.controllersTests;

import com.example.rememberconstellations.controllers.StarsController;
import com.example.rememberconstellations.dto.StarDto;
import com.example.rememberconstellations.services.StarsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class StarsControllerTests {

    @Mock
    private StarsService starsService;

    @InjectMocks
    private StarsController starsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* CREATE */

    @Test
    void testCreateStar_Success() {
        StarDto starDto = new StarDto();
        starDto.setId(1); // Example setting

        when(starsService.createStar(any(StarDto.class))).thenReturn(starDto);

        ResponseEntity<StarDto> result = starsController.createStar(starDto);
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(starDto, result.getBody());

        verify(starsService, times(1)).createStar(any(StarDto.class));
    }

    /* READ */

    @Test
    void testGetStarById_Success() {
        StarDto starDto = new StarDto();
        starDto.setId(1); // Example setting

        when(starsService.getStarById(1)).thenReturn(Optional.of(starDto));

        ResponseEntity<StarDto> result = starsController.getStarById(1);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(starDto, result.getBody());

        verify(starsService, times(1)).getStarById(anyInt());
    }

    @Test
    void testGetStarById_NotFound() {
        when(starsService.getStarById(1)).thenReturn(Optional.empty());

        ResponseEntity<StarDto> result = starsController.getStarById(1);
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(starsService, times(1)).getStarById(anyInt());
    }

    @Test
    void testGetStarsByCriteria_Success() {
        List<StarDto> starDtos = Arrays.asList(new StarDto(), new StarDto());

        when(starsService.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, Pageable.unpaged()))
                .thenReturn(starDtos);

        ResponseEntity<List<StarDto>> result = starsController.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(starDtos, result.getBody());

        verify(starsService, times(1)).getStarsByCriteria(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void testGetStarsByCriteria_NotFound() {
        when(starsService.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, Pageable.unpaged()))
                .thenReturn(List.of());

        ResponseEntity<List<StarDto>> result = starsController.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(starsService, times(1)).getStarsByCriteria(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class));
    }

    /* UPDATE */

    @Test
    void testUpdateStar_Success() {
        StarDto starDto = new StarDto();
        starDto.setId(1);
        starDto.setName("Updated Star");

        when(starsService.updateStar(1, starDto)).thenReturn(Optional.of(starDto));

        ResponseEntity<StarDto> result = starsController.updateStar(1, starDto);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(starDto, result.getBody());

        verify(starsService, times(1)).updateStar(anyInt(), any(StarDto.class));
    }

    @Test
    void testUpdateStar_NotFound() {
        StarDto starDto = new StarDto();
        starDto.setId(1);
        starDto.setName("Updated Star");

        when(starsService.updateStar(1, starDto)).thenReturn(Optional.empty());

        ResponseEntity<StarDto> result = starsController.updateStar(1, starDto);

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(starsService, times(1)).updateStar(anyInt(), any(StarDto.class));
    }

    /* DELETE */

    @Test
    void testDeleteStar_Success() {
        when(starsService.deleteStar(1)).thenReturn(true);

        ResponseEntity<Void> result = starsController.deleteStar(1);

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(starsService, times(1)).deleteStar(anyInt());
    }

    @Test
    void testDeleteStar_NotFound() {
        when(starsService.deleteStar(1)).thenReturn(false);

        ResponseEntity<Void> result = starsController.deleteStar(1);

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(starsService, times(1)).deleteStar(anyInt());
    }
}
