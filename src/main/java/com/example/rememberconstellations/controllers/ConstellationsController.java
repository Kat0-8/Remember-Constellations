package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.services.ConstellationsService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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

    /* CREATE */

    @PostMapping("")
    public ResponseEntity<Constellation> createConstellation(@RequestBody Constellation constellation) {
        Constellation createdConstellation = constellationsService.createConstellation(constellation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConstellation);
    }

    /* READ */

    @GetMapping("/{id}")
    public ResponseEntity<Constellation> getConstellationById(@PathVariable int id) {
        Optional<Constellation> constellation = constellationsService.getConstellationById(id);
        if (constellation.isPresent()) {
            return ResponseEntity.ok(constellation.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Constellation());
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Constellation>> getConstellationByCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String abbreviation,
            @RequestParam(required = false) String family,
            @RequestParam(required = false) String region,
            Pageable pageable) {
        List<Constellation> constellations =
                constellationsService.getConstellationsByCriteria(name, abbreviation, family, region, pageable);
        if (constellations.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(constellations);
        }
    }

    /* UPDATE */

    @PutMapping("/{id}")
    public ResponseEntity<Constellation> updateConstellation(@PathVariable int id, @RequestBody Constellation constellation) {
        Optional<Constellation> updatedConstellation = constellationsService.updateConstellation(id, constellation);
        if (updatedConstellation.isPresent()) {
            return ResponseEntity.ok(updatedConstellation.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /* DELETE */

    @DeleteMapping("/{id}")
    public ResponseEntity<Constellation> deleteConstellation(@PathVariable int id) {
        boolean isDeleted = constellationsService.deleteConstellation(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}


