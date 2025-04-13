package com.example.rememberconstellations.services;

import com.example.rememberconstellations.cache.ConstellationCache;
import com.example.rememberconstellations.dtos.ConstellationDto;
import com.example.rememberconstellations.exceptions.ConstellationAlreadyExistsException;
import com.example.rememberconstellations.exceptions.ResourceNotFoundException;
import com.example.rememberconstellations.mappers.ConstellationMapper;
import com.example.rememberconstellations.mappers.StarMapper;
import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.ConstellationsRepository;
import com.example.rememberconstellations.repositories.StarsRepository;
import com.example.rememberconstellations.utilities.ConstellationSpecification;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class ConstellationsService {

    private final ConstellationsRepository constellationsRepository;
    private final ConstellationMapper constellationMapper;
    private final StarMapper starMapper;
    private final ConstellationCache constellationCache;
    private final StarsRepository starsRepository;

    @Autowired
    public ConstellationsService(ConstellationsRepository constellationsRepository,
                                 ConstellationCache constellationCache,
                                 StarsRepository starsRepository) {
        this.constellationsRepository = constellationsRepository;
        this.starMapper = new StarMapper();
        this.constellationMapper = new ConstellationMapper(starMapper);
        this.constellationCache = constellationCache;
        this.starsRepository = starsRepository;
    }

    /* CREATE */

    @Transactional
    public ConstellationDto createConstellation(ConstellationDto constellationDto) {
        if (constellationsRepository.existsByName(constellationDto.getName())) {
            throw new ConstellationAlreadyExistsException("Constellation with name " + constellationDto.getName() + " already exists");
        }
        log.info("Creating new constellation with name {}", constellationDto.getName());
        Constellation constellation = constellationMapper.mapToEntity(constellationDto);
        Constellation savedConstellation = constellationsRepository.save(constellation);
        ConstellationDto savedConstellationDto = constellationMapper.mapToDto(savedConstellation);
        constellationCache.put(savedConstellation.getId(), savedConstellationDto);
        log.info("Constellation with id {} was saved and cashed", savedConstellationDto.getId());
        return savedConstellationDto;
    }

    @Transactional
    public ConstellationDto attachStars(int id, List<Integer> starIds) {
        Constellation constellation = constellationsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Constellation with id " + id + " not found (attachStars)"));
        List<Star> stars = starsRepository.findByIdAndConstellationIsNull(starIds);
        if (stars.size() != starIds.size()) {
            Set<Integer> foundStarIds = stars.stream()
                    .map(Star::getId)
                    .collect(Collectors.toSet());
            List<Integer> missingStarIds = starIds.stream()
                    .filter(starId -> !foundStarIds.contains(starId))
                    .toList();
            throw new ResourceNotFoundException("Stars not found or already assigned: " + missingStarIds);
        }
        stars.forEach(star -> {
            star.setConstellation(constellation);
            constellation.getStars().add(star);
        });
        starsRepository.saveAll(stars);
        return constellationMapper.mapToDto(constellation);
    }

    /* READ */

    public ConstellationDto getConstellationById(int id) {
        ConstellationDto cashedConstellationDto = constellationCache.get(id);
        if (cashedConstellationDto != null) {
            log.info("Constellation with id {} was retrieved from cache", id);
            return cashedConstellationDto;
        }
        Constellation constellation = constellationsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Constellation with id " + id + " was not found (getConstellationById)"));
        ConstellationDto constellationDto = constellationMapper.mapToDto(constellation);
        constellationCache.put(id, constellationDto);
        log.info("Constellation with id {} was retrieved from repository and cached", id);
        return constellationDto;
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

        List<ConstellationDto> constellationDtos;
        if (pageable != null) {
            constellationDtos = constellationsRepository.findAll(specification, pageable)
                    .getContent()
                    .stream()
                    .map(constellationMapper::mapToDto)
                    .collect(Collectors.toList());
        } else {
            constellationDtos = constellationsRepository.findAll(specification)
                    .stream()
                    .map(constellationMapper::mapToDto)
                    .collect(Collectors.toList());
        }

        for (ConstellationDto constellationDto : constellationDtos) {
            if (constellationCache.get(constellationDto.getId()) == null) {
                constellationCache.put(constellationDto.getId(), constellationDto);
                log.info("Constellation with id {} was added to cache (getConstellationsByCriteria)", constellationDto.getId());
            } else {
                log.info("Constellation with id {} was already in the cache (getConstellationsByCriteria)", constellationDto.getId());
            }
        }
        return constellationDtos;
    }

    public List<ConstellationDto> getConstellationsByStarType(String starType) {
        List<Constellation> constellations = constellationsRepository.findByStarType(starType);
        if (constellations.isEmpty()) {
            throw new ResourceNotFoundException("No constellation with star type " + starType + " was found");
        }
        List<ConstellationDto> constellationDtos = new ArrayList<>();
        for (Constellation constellation : constellations) {
            ConstellationDto constellationDto = constellationMapper.mapToDto(constellation);
            constellationDtos.add(constellationDto);
            if (constellationCache.get(constellation.getId()) == null) {
                constellationCache.put(constellation.getId(), constellationDto);
                log.info("Constellation with id {} was added to cache (getConstellationsByStarType)", constellation.getId());
            } else {
                log.info("Constellation with id {} was already in the cache (getConstellationsByStarType)", constellation.getId());
            }
        }
        return constellationDtos;
    }

    /* UPDATE */

    @Transactional
    public ConstellationDto putConstellation(int id, ConstellationDto constellationDto) {
        if (constellationsRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Constellation with id " + id + " was not found for updating(put)");
        }
        log.info("Updating(put) constellation with id {}", id);
        Constellation constellationToPut = constellationMapper.mapToEntity(constellationDto);
        constellationToPut.setId(id);
        Constellation updatedConstellation = constellationsRepository.save(constellationToPut);
        ConstellationDto updatedConstellationDto = constellationMapper.mapToDto(updatedConstellation);
        constellationCache.put(id, updatedConstellationDto);
        log.info("Constellation with id {} was updated(put) and cache was refreshed", id);
        return updatedConstellationDto;
    }

    @Transactional
    public ConstellationDto patchConstellation(int id, ConstellationDto constellationDto) {
        Constellation constellation = constellationsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No constellation with id " + id + " was found for updating(patch)"));
        log.info("Updating(patch) constellation with id {}", id);
        if (constellationDto.getName() != null) {
            constellation.setName(constellationDto.getName());
        }
        if (constellationDto.getAbbreviation() != null) {
            constellation.setAbbreviation(constellationDto.getAbbreviation());
        }
        if (constellationDto.getFamily() != null) {
            constellation.setFamily(constellationDto.getFamily());
        }
        if (constellationDto.getRegion() != null) {
            constellation.setRegion(constellationDto.getRegion());
        }
        if (constellationDto.getStars() != null) {
            List<Star> oldStars = constellation.getStars();
            List<Star> newStars = constellationDto.getStars().stream()
                    .map(starDto -> {
                        Star star = starMapper.mapToEntity(starDto);
                        star.setConstellation(constellation);
                        return star;
                    })
                    .toList();
            oldStars.addAll(newStars.stream()
                    .filter(newStar -> oldStars.stream().noneMatch(oldStar -> oldStar.getId() == newStar.getId()))
                    .toList());
            constellation.setStars(oldStars);
        }
        Constellation patchedConstellation = constellationsRepository.save(constellation);
        ConstellationDto patchedConstellationDto = constellationMapper.mapToDto(patchedConstellation);
        constellationCache.put(id, patchedConstellationDto);
        log.info("Constellation with id {} was updated(patch) and cache was refreshed", id);
        return patchedConstellationDto;
    }

    /* DELETE */

    @Transactional
    public void deleteConstellation(int id) {
        Constellation constellation = constellationsRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("No constellation with id " + id + " was found for deleting"));
        constellationsRepository.delete(constellation);
        constellationCache.remove(id);
        log.info("Constellation with id {} was deleted and removed from cache", id);
    }
}
