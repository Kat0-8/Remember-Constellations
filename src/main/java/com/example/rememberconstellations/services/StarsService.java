package com.example.rememberconstellations.services;

import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.StarsRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StarsService {
    private final StarsRepository starsRepository;

    @Autowired
    public StarsService(StarsRepository starsRepository) {
        this.starsRepository = starsRepository;
    }

    public Optional<Star> getStarById(final int id) {
        return starsRepository.findStarById(id);
    }
}
