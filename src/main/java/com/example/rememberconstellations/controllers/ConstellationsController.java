package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.dto.ConstellationDto;
import com.example.rememberconstellations.services.ConstellationsService;
import java.util.List;
import java.util.Optional;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Constellation API", description = "Operations on constellations")
@RestController
@RequestMapping(value = "/constellations")
public class ConstellationsController {
    private final ConstellationsService constellationsService;

    @Autowired
    public ConstellationsController(ConstellationsService constellationsService) {
        this.constellationsService = constellationsService;
    }

    /* CREATE */

    @Operation(summary = "Create new constellation")
    @PostMapping("")
    public ResponseEntity<ConstellationDto> createConstellation(@RequestBody ConstellationDto constellationDto) {
        ConstellationDto createdConstellationDto = constellationsService.createConstellation(constellationDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdConstellationDto);
    }

    /* READ */

    @Operation(summary = "Get constellation by id")
    @GetMapping("/{id}")
    public ResponseEntity<ConstellationDto> getConstellationById(@PathVariable int id) {
        Optional<ConstellationDto> constellationDto = constellationsService.getConstellationById(id);
        return constellationDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Get constellation by criteria")
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

    @Operation(summary = "Get constellation by star type")
    @GetMapping("/star-type")
    public ResponseEntity<List<ConstellationDto>> getConstellationsByStarType(@RequestParam String type) {
        List<ConstellationDto> constellationDtos = constellationsService.getConstellationsByStarType(type);
        if (constellationDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(constellationDtos);
        }
    }

    /* UPDATE */

    @Operation(summary = "Put star by id")
    @PutMapping("/{id}")
    public ResponseEntity<ConstellationDto> putConstellation(@PathVariable int id, @RequestBody ConstellationDto constellationDto) {
        Optional<ConstellationDto> putConstellationDto = constellationsService.putConstellation(id, constellationDto);
        return putConstellationDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @Operation(summary = "Patch star by id")
    @PatchMapping("/{id}")
    public ResponseEntity<ConstellationDto> patchConstellation(@PathVariable int id, @RequestBody ConstellationDto constellationDto) {
        Optional<ConstellationDto> patchedConstellationDto = constellationsService.patchConstellation(id, constellationDto);
        return patchedConstellationDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    /* DELETE */

    @Operation(summary = "Delete constellation by id")
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