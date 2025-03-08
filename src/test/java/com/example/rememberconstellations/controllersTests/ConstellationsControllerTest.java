package com.example.rememberconstellations.controllersTests;

import com.example.rememberconstellations.controllers.ConstellationsController;
import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.services.ConstellationsService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class ConstellationsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ConstellationsService constellationsService;

    @InjectMocks
    private ConstellationsController constellationsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(constellationsController).build();
    }

    /* CREATE */

    @Test
    void testCreateConstellation_Success() {
        Constellation newConstellation = new Constellation();
        newConstellation.setId(1); // Set some ID
        when(constellationsService.createConstellation(any(Constellation.class))).thenReturn(newConstellation);

        ResponseEntity<Constellation> result = constellationsController.createConstellation(newConstellation);
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(newConstellation, result.getBody());

        verify(constellationsService, times(1)).createConstellation(any(Constellation.class));
    }

    /* READ */

    @Test
    void testGetConstellationById_Success() {
        Constellation constellation = new Constellation();
        constellation.setId(1); // Example setting

        when(constellationsService.getConstellationById(1)).thenReturn(Optional.of(constellation));

        ResponseEntity<Constellation> result = constellationsController.getConstellationById(1);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(constellation, result.getBody());

        verify(constellationsService, times(1)).getConstellationById(anyInt());
    }

    @Test
    void testGetConstellationById_NotFound() {
        when(constellationsService.getConstellationById(1)).thenReturn(Optional.empty());

        ResponseEntity<Constellation> result = constellationsController.getConstellationById(1);
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNotNull(result.getBody()); // The body should be an empty Constellation object

        verify(constellationsService, times(1)).getConstellationById(anyInt());
    }

    @Test
    void testGetConstellationsByCriteria_Success() {
        List<Constellation> constellations = Arrays.asList(new Constellation(), new Constellation());

        when(constellationsService.getConstellationsByCriteria(null, null, null, null, Pageable.unpaged()))
                .thenReturn(constellations);

        ResponseEntity<List<Constellation>> result = constellationsController.getConstellationByCriteria(null, null, null, null, Pageable.unpaged());
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(constellations, result.getBody());

        verify(constellationsService, times(1)).getConstellationsByCriteria(any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void testGetConstellationsByCriteria_NotFound() {
        when(constellationsService.getConstellationsByCriteria(null, null, null, null, Pageable.unpaged()))
                .thenReturn(List.of());

        ResponseEntity<List<Constellation>> result = constellationsController.getConstellationByCriteria(null, null, null, null, Pageable.unpaged());
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(constellationsService, times(1)).getConstellationsByCriteria(any(), any(), any(), any(), any(Pageable.class));
    }

    /* UPDATE  */

    @Test
    void testUpdateConstellation_Success() {
        Constellation updatedConstellation = new Constellation();
        updatedConstellation.setId(1); // Existing constellation ID
        when(constellationsService.updateConstellation(anyInt(), any(Constellation.class)))
                .thenReturn(Optional.of(updatedConstellation));

        ResponseEntity<Constellation> result = constellationsController.updateConstellation(1, updatedConstellation);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedConstellation, result.getBody());

        verify(constellationsService, times(1)).updateConstellation(anyInt(), any(Constellation.class));
    }

    @Test
    void testUpdateConstellation_NotFound() {
        Constellation updatedConstellation = new Constellation();
        updatedConstellation.setId(1);
        when(constellationsService.updateConstellation(anyInt(), any(Constellation.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<Constellation> result = constellationsController.updateConstellation(1, updatedConstellation);
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(constellationsService, times(1)).updateConstellation(anyInt(), any(Constellation.class));
    }

    /* DELETE */

    @Test
    void testDeleteConstellation_Success() {
        when(constellationsService.deleteConstellation(anyInt())).thenReturn(true);

        ResponseEntity<Constellation> result = constellationsController.deleteConstellation(1);
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(constellationsService, times(1)).deleteConstellation(anyInt());
    }

    @Test
    void testDeleteConstellation_NotFound() {
        when(constellationsService.deleteConstellation(anyInt())).thenReturn(false);

        ResponseEntity<Constellation> result = constellationsController.deleteConstellation(1);
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(constellationsService, times(1)).deleteConstellation(anyInt());
    }
}
