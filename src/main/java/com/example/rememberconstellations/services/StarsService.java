package com.example.rememberconstellations.services;

import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.StarsRepository;
import com.example.rememberconstellations.utilities.StarSpecification;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class StarsService {
    private final StarsRepository starsRepository;

    @Autowired
    public StarsService(StarsRepository starsRepository) {
        this.starsRepository = starsRepository;
    }

    /* CREATE */

    public Star createStar(Star star) {
        return starsRepository.save(star);
    }

    /* READ */

    public Optional<Star> getStarById(final int id) {
        return starsRepository.findStarById(id);
    }

    @SuppressWarnings("java:S107")
    public List<Star> getStarsByCriteria(String name, String type, Double mass, Double radius,
                                         Double temperature, Double luminosity, Double rightAscension,
                                         Double declination, String positionInConstellation,
                                         Integer constellationId, Pageable pageable) {
        Specification<Star> specification = Specification.where(null);

        if (name != null) {
            specification = specification.and(StarSpecification.withName(name));
        }
        if (type != null) {
            specification = specification.and(StarSpecification.withType(type));
        }
        if (mass != null) {
            specification = specification.and(StarSpecification.withMassGreaterThanOrEqual(mass));
        }
        if (radius != null) {
            specification = specification.and(StarSpecification.withRadiusGreaterThanOrEqual(radius));
        }
        if (temperature != null) {
            specification = specification.and(StarSpecification.withTemperatureGreaterThanOrEqual(temperature));
        }
        if (luminosity != null) {
            specification = specification.and(StarSpecification.withLuminosityGreaterThanOrEqual(luminosity));
        }
        if (rightAscension != null) {
            specification = specification.and(StarSpecification.withRightAscensionGreaterThanOrEqual(rightAscension));
        }
        if (declination != null) {
            specification = specification.and(StarSpecification.withDeclinationGreaterThanOrEqual(declination));
        }
        if (positionInConstellation != null) {
            specification = specification.and(StarSpecification.withPositionInConstellation(positionInConstellation));
        }
        if (constellationId != null) {
            specification = specification.and(StarSpecification.withConstellationId(constellationId));
        }

        if (pageable != null) {
            return starsRepository.findAll(specification, pageable).getContent();
        } else {
            return starsRepository.findAll(specification);
        }
    }

    /* UPDATE */

    public Optional<Star> updateStar(int id, Star star) {
        if (starsRepository.existsById(id)) {
            star.setId(id);
            return Optional.of(starsRepository.save(star));
        } else {
            return Optional.empty();
        }
    }

    /* DELETE */

    public boolean deleteStar(int id) {
        if (starsRepository.existsById(id)) {
            starsRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
