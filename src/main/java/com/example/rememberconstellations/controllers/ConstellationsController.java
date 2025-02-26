package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.services.ConstellationsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/constellations")
public class ConstellationsController {
    private final ConstellationsService constellationsService;

    @Autowired
    public ConstellationsController(ConstellationsService constellationsService) {
        this.constellationsService = constellationsService;
    }

    @GetMapping("/get/byName")
    public Constellation getConstellationByName(@RequestParam String name) {
        return constellationsService.getConstellationByName(name);
    }

    @GetMapping("/get/byAbbreviation/{abbreviation}")
    public ResponseEntity<Constellation> getConstellationByAbbreviation(@PathVariable String abbreviation) {
        Constellation constellation = constellationsService.getConstellationByAbbreviation(abbreviation);
        if (constellation == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(constellation);
        }
    }

    @GetMapping("/get/all")
    public List<Constellation> getAllConstellations() {
        return constellationsService.getConstellations();
    }
}


