package com.example.rememberconstellations.controllersTests;

import com.example.rememberconstellations.controllers.ConstellationsController;
import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.services.ConstellationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ConstellationsControllerTests {
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

    @Test
    void testGetConstellationByName_Found() throws Exception {
        Constellation constellation = new Constellation();
        constellation.setName("Orion");
        when(constellationsService.getConstellationByName(anyString())).thenReturn(constellation);

        mockMvc.perform(get("/constellations/get/byName")
                        .param("name", "Orion"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"Orion\"}"));

        verify(constellationsService, times(1)).getConstellationByName("Orion");
    }

    @Test
    void testGetConstellationByName_NotFound() throws Exception {
        when(constellationsService.getConstellationByName(anyString())).thenReturn(null);

        mockMvc.perform(get("/constellations/get/byName")
                        .param("name", "Orion"))
                .andExpect(status().isNotFound());

        verify(constellationsService, times(1)).getConstellationByName("Orion");
    }

    @Test
    void testGetConstellationByAbbreviation_Found() throws Exception {
        Constellation constellation = new Constellation();
        constellation.setAbbreviation("ORI");
        when(constellationsService.getConstellationByAbbreviation(anyString())).thenReturn(constellation);

        mockMvc.perform(get("/constellations/get/byAbbreviation/{abbreviation}", "ORI"))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"abbreviation\":\"ORI\"}"));

        verify(constellationsService, times(1)).getConstellationByAbbreviation("ORI");
    }

    @Test
    void testGetConstellationByAbbreviation_NotFound() throws Exception {
        when(constellationsService.getConstellationByAbbreviation(anyString())).thenReturn(null);

        mockMvc.perform(get("/constellations/get/byAbbreviation/{abbreviation}", "ORI"))
                .andExpect(status().isNotFound());

        verify(constellationsService, times(1)).getConstellationByAbbreviation("ORI");
    }

    @Test
    void testGetAllConstellations_Found() throws Exception {
        Constellation constellation1 = new Constellation();
        constellation1.setName("Orion");
        Constellation constellation2 = new Constellation();
        constellation2.setName("Andromeda");

        List<Constellation> constellations = Arrays.asList(constellation1, constellation2);
        when(constellationsService.getConstellations()).thenReturn(constellations);

        mockMvc.perform(get("/constellations/get/all"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"name\":\"Orion\"}, {\"name\":\"Andromeda\"}]"));

        verify(constellationsService, times(1)).getConstellations();
    }

    @Test
    void testGetAllConstellations_Empty() throws Exception {
        when(constellationsService.getConstellations()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/constellations/get/all"))
                .andExpect(status().isNotFound());

        verify(constellationsService, times(1)).getConstellations();
    }
}
