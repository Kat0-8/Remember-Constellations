package com.example.rememberconstellations.services;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
import com.example.rememberconstellations.utilities.ConstellationSpecification;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class ConstellationsService {

    private final ConstellationsRepository constellationsRepository;

    @Autowired
    public ConstellationsService(ConstellationsRepository constellationsRepository) {
        this.constellationsRepository = constellationsRepository;
    }

    /* CREATE */

    public Constellation createConstellation(Constellation constellation) {
        return constellationsRepository.save(constellation);
    }

    /* READ */

    public Optional<Constellation> getConstellationById(final int id) {
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

    public Optional<Constellation> updateConstellation(int id, Constellation constellation) {
        if (constellationsRepository.existsById(id)) {
            constellation.setId(id);
            return Optional.of(constellationsRepository.save(constellation));
        } else {
            return Optional.empty();
        }
    }

    /* DELETE */

    public boolean deleteConstellation(int id) {
        if (constellationsRepository.existsById(id)) {
            constellationsRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
