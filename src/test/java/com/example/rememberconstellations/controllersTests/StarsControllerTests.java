package com.example.rememberconstellations.controllersTests;

import com.example.rememberconstellations.controllers.StarsController;
import com.example.rememberconstellations.models.Star;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.any;
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
        Star star = new Star();
        star.setId(1); // Example setting

        when(starsService.createStar(any(Star.class))).thenReturn(star);

        ResponseEntity<Star> result = starsController.createStar(star);
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(star, result.getBody());

        verify(starsService, times(1)).createStar(any(Star.class));
    }

    /* READ */

    @Test
    void testGetStarById_Success() {
        Star star = new Star();
        star.setId(1); // Example setting

        when(starsService.getStarById(1)).thenReturn(Optional.of(star));

        ResponseEntity<Star> result = starsController.getStarById(1);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(star, result.getBody());

        verify(starsService, times(1)).getStarById(anyInt());
    }

    @Test
    void testGetStarById_NotFound() {
        when(starsService.getStarById(1)).thenReturn(Optional.empty());

        ResponseEntity<Star> result = starsController.getStarById(1);
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(starsService, times(1)).getStarById(anyInt());
    }

    @Test
    void testGetStarsByCriteria_Success() {
        List<Star> stars = Arrays.asList(new Star(), new Star());

        when(starsService.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, Pageable.unpaged()))
                .thenReturn(stars);

        ResponseEntity<List<Star>> result = starsController.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(stars, result.getBody());

        verify(starsService, times(1)).getStarsByCriteria(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void testGetStarsByCriteria_NotFound() {
        when(starsService.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, Pageable.unpaged()))
                .thenReturn(List.of());

        ResponseEntity<List<Star>> result = starsController.getStarsByCriteria(
                null, null, null, null, null, null, null, null, null, null, Pageable.unpaged());

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(starsService, times(1)).getStarsByCriteria(any(), any(), any(), any(), any(), any(), any(), any(), any(), any(), any(Pageable.class));
    }

    /* UPDATE */

    @Test
    void testUpdateStar_Success() {
        Star star = new Star();
        star.setId(1);
        star.setName("Updated Star");

        when(starsService.updateStar(1, star)).thenReturn(Optional.of(star));

        ResponseEntity<Star> result = starsController.updateStar(1, star);

        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(star, result.getBody());

        verify(starsService, times(1)).updateStar(anyInt(), any(Star.class));
    }

    @Test
    void testUpdateStar_NotFound() {
        Star star = new Star();
        star.setId(1);
        star.setName("Updated Star");

        when(starsService.updateStar(1, star)).thenReturn(Optional.empty());

        ResponseEntity<Star> result = starsController.updateStar(1, star);

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(starsService, times(1)).updateStar(anyInt(), any(Star.class));
    }

    /* DELETE */

    @Test
    void testDeleteStar_Success() {
        when(starsService.deleteStar(1)).thenReturn(true);

        ResponseEntity<Star> result = starsController.deleteStar(1);

        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(starsService, times(1)).deleteStar(anyInt());
    }

    @Test
    void testDeleteStar_NotFound() {
        when(starsService.deleteStar(1)).thenReturn(false);

        ResponseEntity<Star> result = starsController.deleteStar(1);

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(starsService, times(1)).deleteStar(anyInt());
    }
}
