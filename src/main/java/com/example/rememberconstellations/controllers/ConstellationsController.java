package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.dto.ConstellationDto;
import com.example.rememberconstellations.services.ConstellationsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    public ConstellationDto createConstellation(@Valid @RequestBody ConstellationDto constellationDto) {
        return constellationsService.createConstellation(constellationDto);
    }

    /* READ */

    @Operation(summary = "Get constellation by id")
    @GetMapping("/{id}")
    public ConstellationDto getConstellationById(@PathVariable int id) {
        return constellationsService.getConstellationById(id);
    }

    @Operation(summary = "Get constellation by criteria")
    @GetMapping("")
    public List<ConstellationDto> getConstellationByCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String abbreviation,
            @RequestParam(required = false) String family,
            @RequestParam(required = false) String region,
            Pageable pageable) {
        return constellationsService.getConstellationsByCriteria(name, abbreviation, family, region, pageable);
    }

    @Operation(summary = "Get constellation by star type")
    @GetMapping("/star-type")
    public List<ConstellationDto> getConstellationsByStarType(@RequestParam String type) {
        return constellationsService.getConstellationsByStarType(type);
    }

    /* UPDATE */

    @Operation(summary = "Put star by id")
    @PutMapping("/{id}")
    public ConstellationDto putConstellation(@PathVariable int id, @RequestBody ConstellationDto constellationDto) {
        return constellationsService.putConstellation(id, constellationDto);
    }

    @Operation(summary = "Patch star by id")
    @PatchMapping("/{id}")
    public ConstellationDto patchConstellation(@PathVariable int id, @RequestBody ConstellationDto constellationDto) {
        return constellationsService.patchConstellation(id, constellationDto);
    }

    /* DELETE */

    @Operation(summary = "Delete constellation by id")
    @DeleteMapping("/{id}")
    public void deleteConstellation(@PathVariable int id) {
        constellationsService.deleteConstellation(id);
    }
}