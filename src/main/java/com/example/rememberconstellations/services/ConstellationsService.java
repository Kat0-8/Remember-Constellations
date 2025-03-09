package com.example.rememberconstellations.services;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
import com.example.rememberconstellations.repositories.StarsRepository;
import com.example.rememberconstellations.utilities.ConstellationSpecification;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ConstellationsService {

    private final ConstellationsRepository constellationsRepository;
    private final StarsRepository starsRepository;

    @Autowired
    public ConstellationsService(ConstellationsRepository constellationsRepository, StarsRepository starsRepository) {
        this.constellationsRepository = constellationsRepository;
        this.starsRepository = starsRepository;
    }

    /* CREATE */

    @Transactional
    public Constellation createConstellation(Constellation constellation) {
        List<Star> stars = constellation.getStars();
        for (Star star : stars) {
            if (star.getId() == 0) {
                star.setConstellation(constellation);
                starsRepository.save(star);
            } else {
                Star existingStar = starsRepository.findById(star.getId())
                        .orElseThrow(() -> new RuntimeException("Star not found"));
                existingStar.setConstellation(constellation);
                starsRepository.save(existingStar);
            }
        }
        return constellationsRepository.save(constellation);
    }

    /* READ */

    public Optional<Constellation> getConstellationById(int id) {
        return constellationsRepository.findById(id);
    }

    public List<Constellation> getConstellationsByCriteria(String name, String abbreviation,
                                                           String family, String region, Pageable pageable) {
        Specification<Constellation> specification = Specification.where(null);

        if (name != null) {
            specification = specification.and(ConstellationSpecification.withName(name));
        }
        if (abbreviation != null) {
            specification = specification.and(ConstellationSpecification.withAbbreviation(abbreviation));
        }
        if (family != null) {
            specification = specification.and(ConstellationSpecification.withFamily(family));
        }
        if (region != null) {
            specification = specification.and(ConstellationSpecification.withRegion(region));
        }

        if (pageable != null) {
            return constellationsRepository.findAll(specification, pageable).getContent();
        } else {
            return constellationsRepository.findAll(specification);
        }
    }

    /* UPDATE */

    @Transactional
    public Optional<Constellation> updateConstellation(int id, Constellation constellation) {
        if (constellationsRepository.existsById(id)) {
            constellation.setId(id);
            return Optional.of(constellationsRepository.save(constellation));
        } else {
            return Optional.empty();
        }
    }

    /* DELETE */

    @Transactional
    public boolean deleteConstellation(int id) {
        if (constellationsRepository.existsById(id)) {
            Constellation constellationToDelete = constellationsRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Something is wrong: no constellation found with id: " + id));
            List<Star> starsToDetach = constellationToDelete.getStars();
            for (Star starToDetach : starsToDetach) {
                starToDetach.setConstellation(null);
                starToDetach.setPositionInConstellation(null);
            }
            constellationsRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
