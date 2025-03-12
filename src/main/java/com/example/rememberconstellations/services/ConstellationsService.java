package com.example.rememberconstellations.services;

import com.example.rememberconstellations.dto.ConstellationDto;
import com.example.rememberconstellations.mappers.ConstellationMapper;
import com.example.rememberconstellations.mappers.StarMapper;
import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
import com.example.rememberconstellations.utilities.ConstellationSpecification;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class ConstellationsService {

    private final ConstellationsRepository constellationsRepository;
    private final ConstellationMapper constellationMapper;

    @Autowired
    public ConstellationsService(ConstellationsRepository constellationsRepository) {
        this.constellationsRepository = constellationsRepository;
        this.constellationMapper = new ConstellationMapper(new StarMapper());
    }

    /* CREATE */

    @Transactional
    public ConstellationDto createConstellation(ConstellationDto constellationDto) {
        Constellation constellation = constellationMapper.mapToEntity(constellationDto);
        Constellation savedConstellation = constellationsRepository.save(constellation);
        return constellationMapper.mapToDto(savedConstellation);
    }

    /* READ */

    public Optional<ConstellationDto> getConstellationById(int id) {
        return constellationsRepository.findById(id)
                .map(constellationMapper::mapToDto);
    }

    public List<ConstellationDto> getConstellationsByCriteria(String name, String abbreviation,
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
            return constellationsRepository.findAll(specification, pageable).getContent().stream()
                    .map(constellationMapper::mapToDto)
                    .collect(Collectors.toList());
        } else {
            return constellationsRepository.findAll(specification).stream()
                    .map(constellationMapper::mapToDto)
                    .collect(Collectors.toList());
        }
    }

    /* UPDATE */

    @Transactional
    public Optional<ConstellationDto> updateConstellation(int id, ConstellationDto constellationDto) {
        if (constellationsRepository.existsById(id)) {
            Constellation constellation = constellationMapper.mapToEntity(constellationDto);
            constellation.setId(id);
            Constellation updatedConstellation = constellationsRepository.save(constellation);
            return Optional.of(constellationMapper.mapToDto(updatedConstellation));
        } else {
            return Optional.empty();
        }
    }

    /* DELETE */

    @Transactional
    public boolean deleteConstellation(int id) {
        Optional<Constellation> constellationOptional = constellationsRepository.findById(id);
        if (constellationOptional.isPresent()) {
            constellationsRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
