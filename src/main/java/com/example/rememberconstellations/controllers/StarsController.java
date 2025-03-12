package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.dto.StarDto;
import com.example.rememberconstellations.services.StarsService;
import java.util.List;
import java.util.Optional;
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

@RestController
@RequestMapping(value = "/stars")
public class StarsController {
    private final StarsService starsService;

    @Autowired
    public StarsController(StarsService starsService) {
        this.starsService = starsService;
    }

    /* CREATE */

    @PostMapping("")
    public ResponseEntity<StarDto> createStar(@RequestBody StarDto starDto) {
        StarDto createdStarDto = starsService.createStar(starDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStarDto);
    }

    /* READ */

    @GetMapping("/{id}")
    public ResponseEntity<StarDto> getStarById(@PathVariable int id) {
        Optional<StarDto> starDto = starsService.getStarById(id);
        return starDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("")
    public ResponseEntity<List<StarDto>> getStarsByCriteria(
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
        List<StarDto> starDtos =
                starsService.getStarsByCriteria(name, type, mass, radius, temperature, luminosity,
                                                rightAscension, declination, positionInConstellation,
                                                constellationId, pageable);
        if (starDtos.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(starDtos);
        }
    }

    /* UPDATE */

    @PutMapping("/{id}")
    public ResponseEntity<StarDto> putStar(@PathVariable int id, @RequestBody StarDto starDto) {
        Optional<StarDto> putStarDto = starsService.putStar(id, starDto);
        return putStarDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}")
    public ResponseEntity<StarDto> patchStar(@PathVariable int id, @RequestBody StarDto starDto) {
        Optional<StarDto> patchedStarDto = starsService.patchStar(id, starDto);
        return patchedStarDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /* DELETE */

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStar(@PathVariable int id) {
        boolean isDeleted = starsService.deleteStar(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
