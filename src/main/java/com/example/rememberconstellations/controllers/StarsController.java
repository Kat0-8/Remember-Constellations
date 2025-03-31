package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.dto.StarDto;
import com.example.rememberconstellations.services.StarsService;
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

@Tag(name = "Stars API", description = "Operations on stars")
@RestController
@RequestMapping(value = "/stars")
public class StarsController {
    private final StarsService starsService;

    @Autowired
    public StarsController(StarsService starsService) {
        this.starsService = starsService;
    }

    /* CREATE */

    @Operation(summary = "Create new star")
    @PostMapping("")
    public StarDto createStar(@Valid @RequestBody StarDto starDto) {
        return starsService.createStar(starDto);
    }

    @Operation(summary = "Create several stars in bulk")
    @PostMapping("/bulk")
    public List<StarDto> createStars(@Valid @RequestBody List<StarDto> starDtos) {
        return starsService.createStars(starDtos);
    }

    /* READ */

    @Operation(summary = "Get star by id")
    @GetMapping("/{id}")
    public StarDto getStarById(@PathVariable int id) {
        return starsService.getStarById(id);
    }

    @Operation(summary = "Get star by criteria")
    @GetMapping("")
    public List<StarDto> getStarsByCriteria(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double mass,
            @RequestParam(required = false) Double radius,
            @RequestParam(required = false) Double temperature,
            @RequestParam(required = false) Double luminosity,
            @RequestParam(required = false) Double rightAscension,
            @RequestParam(required = false) Double declination,
            @RequestParam(required = false) String positionInConstellation,
            @RequestParam(required = false) Integer constellationId,
            Pageable pageable) {
        return starsService.getStarsByCriteria(name, type, mass, radius, temperature, luminosity,
                                                rightAscension, declination, positionInConstellation,
                                                constellationId, pageable);
    }

    /* UPDATE */

    @Operation(summary = "Put star by id")
    @PutMapping("/{id}")
    public StarDto putStar(@PathVariable int id, @RequestBody StarDto starDto) {
        return starsService.putStar(id, starDto);
    }

    @Operation(summary = "Patch star by id")
    @PatchMapping("/{id}")
    public StarDto patchStar(@PathVariable int id, @RequestBody StarDto starDto) {
        return starsService.patchStar(id, starDto);
    }

    /* DELETE */

    @Operation(summary = "Delete star by id")
    @DeleteMapping("/{id}")
    public void deleteStar(@PathVariable int id) {
        starsService.deleteStar(id);
    }
}
