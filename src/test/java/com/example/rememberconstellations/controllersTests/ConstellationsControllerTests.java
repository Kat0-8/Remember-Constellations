package com.example.rememberconstellations.controllersTests;

import com.example.rememberconstellations.controllers.ConstellationsController;
import com.example.rememberconstellations.dto.ConstellationDto;
import com.example.rememberconstellations.services.ConstellationsService;
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

class ConstellationsControllerTests {

    @Mock
    private ConstellationsService constellationsService;

    @InjectMocks
    private ConstellationsController constellationsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* CREATE */

    @Test
    void testCreateConstellation_Success() {
        ConstellationDto newConstellationDto = new ConstellationDto();
        newConstellationDto.setId(1); // Set some ID
        when(constellationsService.createConstellation(any(ConstellationDto.class))).thenReturn(newConstellationDto);

        ResponseEntity<ConstellationDto> result = constellationsController.createConstellation(newConstellationDto);
        assertNotNull(result);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        assertEquals(newConstellationDto, result.getBody());

        verify(constellationsService, times(1)).createConstellation(any(ConstellationDto.class));
    }

    /* READ */

    @Test
    void testGetConstellationById_Success() {
        ConstellationDto constellationDto = new ConstellationDto();
        constellationDto.setId(1); // Example setting

        when(constellationsService.getConstellationById(1)).thenReturn(Optional.of(constellationDto));

        ResponseEntity<ConstellationDto> result = constellationsController.getConstellationById(1);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(constellationDto, result.getBody());

        verify(constellationsService, times(1)).getConstellationById(anyInt());
    }

    @Test
    void testGetConstellationById_NotFound() {
        when(constellationsService.getConstellationById(1)).thenReturn(Optional.empty());

        ResponseEntity<ConstellationDto> result = constellationsController.getConstellationById(1);
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(constellationsService, times(1)).getConstellationById(anyInt());
    }

    @Test
    void testGetConstellationsByCriteria_Success() {
        List<ConstellationDto> constellationDtos = Arrays.asList(new ConstellationDto(), new ConstellationDto());

        when(constellationsService.getConstellationsByCriteria(null, null, null, null, Pageable.unpaged()))
                .thenReturn(constellationDtos);

        ResponseEntity<List<ConstellationDto>> result = constellationsController.getConstellationByCriteria(null, null, null, null, Pageable.unpaged());
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(constellationDtos, result.getBody());

        verify(constellationsService, times(1)).getConstellationsByCriteria(any(), any(), any(), any(), any(Pageable.class));
    }

    @Test
    void testGetConstellationsByCriteria_NotFound() {
        when(constellationsService.getConstellationsByCriteria(null, null, null, null, Pageable.unpaged()))
                .thenReturn(List.of());

        ResponseEntity<List<ConstellationDto>> result = constellationsController.getConstellationByCriteria(null, null, null, null, Pageable.unpaged());
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(constellationsService, times(1)).getConstellationsByCriteria(any(), any(), any(), any(), any(Pageable.class));
    }

    /* UPDATE  */

    @Test
    void testPutConstellation_Success() {
        ConstellationDto updatedConstellationDto = new ConstellationDto();
        updatedConstellationDto.setId(1); // Existing constellation ID
        when(constellationsService.putConstellation(anyInt(), any(ConstellationDto.class)))
                .thenReturn(Optional.of(updatedConstellationDto));

        ResponseEntity<ConstellationDto> result = constellationsController.putConstellation(1, updatedConstellationDto);
        assertNotNull(result);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(updatedConstellationDto, result.getBody());

        verify(constellationsService, times(1)).putConstellation(anyInt(), any(ConstellationDto.class));
    }

    @Test
    void testPutConstellation_NotFound() {
        ConstellationDto updatedConstellationDto = new ConstellationDto();
        updatedConstellationDto.setId(1);
        when(constellationsService.putConstellation(anyInt(), any(ConstellationDto.class)))
                .thenReturn(Optional.empty());

        ResponseEntity<ConstellationDto> result = constellationsController.putConstellation(1, updatedConstellationDto);
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(constellationsService, times(1)).putConstellation(anyInt(), any(ConstellationDto.class));
    }

    /* DELETE */

    @Test
    void testDeleteConstellation_Success() {
        when(constellationsService.deleteConstellation(anyInt())).thenReturn(true);

        ResponseEntity<Void> result = constellationsController.deleteConstellation(1);
        assertNotNull(result);
        assertEquals(HttpStatus.NO_CONTENT, result.getStatusCode());

        verify(constellationsService, times(1)).deleteConstellation(anyInt());
    }

    @Test
    void testDeleteConstellation_NotFound() {
        when(constellationsService.deleteConstellation(anyInt())).thenReturn(false);

        ResponseEntity<Void> result = constellationsController.deleteConstellation(1);
        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());

        verify(constellationsService, times(1)).deleteConstellation(anyInt());
    }
}
