package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.services.StarsService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
            @RequestParam(required = false) double mass,
            @RequestParam(required = false) double radius,
            @RequestParam(required = false) double temperature,
            @RequestParam(required = false) double luminosity,
            @RequestParam(required = false) double rightAscension,
            @RequestParam(required = false) double declination,
            @RequestParam(required = false) String positionInConstellation,
            Pageable pageable) {
        List<Star> stars =
                starsService.getStarsByCriteria(name, type, mass, radius, temperature, luminosity,
                                                rightAscension, declination, positionInConstellation, pageable);
        if (stars.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(stars);
        }
    }

}
