package com.example.rememberconstellations.services;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ConstellationsService {

    private final ConstellationsRepository constellationsRepository;

    @Autowired
    public ConstellationsService(ConstellationsRepository constellationsRepository) {
        this.constellationsRepository = constellationsRepository;
    }

    public List<Constellation> getConstellations() {
        return constellationsRepository.getAllConstellations();
    }

    public Constellation getConstellationByName(final String name) {
        return constellationsRepository.getConstellationByName(name);
    }

    public Constellation getConstellationByAbbreviation(final String abbreviation) {
        return constellationsRepository.getConstellationByAbbreviation(abbreviation);
    }

}
