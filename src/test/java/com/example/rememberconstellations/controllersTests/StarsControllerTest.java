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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class StarsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StarsService starsService;

    @InjectMocks
    private StarsController starsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(starsController).build();
    }

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
}
