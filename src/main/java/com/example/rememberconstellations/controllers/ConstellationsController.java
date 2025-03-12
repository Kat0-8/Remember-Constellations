package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.dto.ConstellationDto;
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
    public ResponseEntity<ConstellationDto> createConstellation(@RequestBody ConstellationDto constellationDto) {
        ConstellationDto createdConstellationDto = constellationsService.createConstellation(constellationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConstellationDto);
    }

    /* READ */

    @GetMapping("/{id}")
    public ResponseEntity<ConstellationDto> getConstellationById(@PathVariable int id) {
        Optional<ConstellationDto> constellationDto = constellationsService.getConstellationById(id);
        if (constellationDto.isPresent()) {
            return ResponseEntity.ok(constellationDto.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<ConstellationDto>> getConstellationByCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String abbreviation,
            @RequestParam(required = false) String family,
            @RequestParam(required = false) String region,
            Pageable pageable) {
        List<ConstellationDto> constellationDtos =
                constellationsService.getConstellationsByCriteria(name, abbreviation, family, region, pageable);
        if (constellationDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(constellationDtos);
        }
    }

    /* UPDATE */

    @PutMapping("/{id}")
    public ResponseEntity<ConstellationDto> updateConstellation(@PathVariable int id, @RequestBody ConstellationDto constellationDto) {
        Optional<ConstellationDto> updatedConstellationDto = constellationsService.updateConstellation(id, constellationDto);
        if (updatedConstellationDto.isPresent()) {
            return ResponseEntity.ok(updatedConstellationDto.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /* DELETE */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConstellation(@PathVariable int id) {
        boolean isDeleted = constellationsService.deleteConstellation(id);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}