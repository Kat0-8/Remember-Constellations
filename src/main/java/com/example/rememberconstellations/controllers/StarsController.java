package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.services.StarsService;
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
@RequestMapping(value = "/stars")
public class StarsController {
    private final StarsService starsService;

    @Autowired
    public StarsController(StarsService starsService) {
        this.starsService = starsService;
    }

    /* CREATE */

    @PostMapping("")
    public ResponseEntity<Star> createStar(@RequestBody Star star) {
        Star createdStar = starsService.createStar(star);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdStar);
    }

    /* READ */

    @GetMapping("/{id}")
    public ResponseEntity<Star> getStarById(@PathVariable int id) {
        Optional<Star> star = starsService.getStarById(id);
        if (star.isPresent()) {
            return ResponseEntity.ok(star.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("")
    public ResponseEntity<List<Star>> getStarsByCriteria(
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
        List<Star> stars =
                starsService.getStarsByCriteria(name, type, mass, radius, temperature, luminosity,
                                                rightAscension, declination, positionInConstellation,
                                                constellationId, pageable);
        if (stars.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(stars);
        }
    }

    /* UPDATE */

    @PutMapping("/{id}")
    public ResponseEntity<Star> updateStar(@PathVariable int id, @RequestBody Star star) {
        Optional<Star> updatedStar = starsService.updateStar(id, star);
        if (updatedStar.isPresent()) {
            return ResponseEntity.ok(updatedStar.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /* DELETE */

    @DeleteMapping("/{id}")
    public ResponseEntity<Star> deleteStar(@PathVariable int id) {
        boolean isDeleted = starsService.deleteStar(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
