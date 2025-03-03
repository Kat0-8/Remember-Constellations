package com.example.rememberconstellations.controllers;

import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.services.StarsService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

}
