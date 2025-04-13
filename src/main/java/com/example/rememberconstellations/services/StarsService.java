package com.example.rememberconstellations.services;

import com.example.rememberconstellations.cache.StarCache;
import com.example.rememberconstellations.dtos.StarDto;
import com.example.rememberconstellations.exceptions.ResourceNotFoundException;
import com.example.rememberconstellations.exceptions.StarAlreadyExistsException;
import com.example.rememberconstellations.mappers.StarMapper;
import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.StarsRepository;
import com.example.rememberconstellations.utilities.specifications.StarSpecification;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class StarsService {
    private final StarsRepository starsRepository;
    private final StarMapper starMapper;
    private final StarCache starCache;

    @Autowired
    public StarsService(StarsRepository starsRepository, StarMapper starMapper, StarCache starCache) {
        this.starsRepository = starsRepository;
        this.starMapper = starMapper;
        this.starCache = starCache;
    }

    /* CREATE */

    @Transactional
    public StarDto createStar(StarDto starDto) {
        if (starsRepository.existsByName(starDto.getName())) {
            throw new StarAlreadyExistsException("Star with name " + starDto.getName() + " already exists");
        }
        log.info("Creating new star with name {}", starDto.getName());
        Star star = starMapper.mapToEntity(starDto);
        Star savedStar = starsRepository.save(star);
        StarDto savedStarDto = starMapper.mapToDto(savedStar);
        starCache.put(savedStar.getId(), savedStarDto);
        log.info("Star with id {} was saved and cached", savedStarDto.getId());
        return savedStarDto;
    }

    @Transactional
    public List<StarDto> createStars(List<StarDto> starDtos) {
        List<String> names = starDtos.stream()
                .map(StarDto::getName)
                .toList();
        List<String> existingStarsNames = starsRepository.findExistingStarsNamesIn(names);
        if (!existingStarsNames.isEmpty()) {
            throw new StarAlreadyExistsException("Star with name " + existingStarsNames + " already exists");
        }
        log.info("Creating {} stars in bulk", starDtos.size());
        List<Star> stars = starDtos.stream()
                .map(starMapper::mapToEntity)
                .toList();
        List<Star> savedStars = starsRepository.saveAll(stars);
        List<StarDto> savedStarDtos = savedStars.stream()
                .map(star -> {
                    StarDto savedStarDto = starMapper.mapToDto(star);
                    starCache.put(savedStarDto.getId(), savedStarDto);
                    return savedStarDto;
                })
                .toList();
        log.info("{} stars were saved and cached", savedStarDtos.size());
        return savedStarDtos;
    }

    /* READ */

    public StarDto getStarById(final int id) {
        StarDto cashedStarDto = starCache.get(id);
        if (cashedStarDto != null) {
            log.info("Star with id {} was retrieved from cache", id);
            return cashedStarDto;
        }
        Star star = starsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Star with id " + id + " was not found"));
        StarDto starDto = starMapper.mapToDto(star);
        starCache.put(id, starDto);
        log.info("Star with id {} was retrieved from repository and cashed", id);
        return starDto;
    }

    @SuppressWarnings("java:S107")
    public List<StarDto> getStarsByCriteria(String name, String type, Double mass, Double radius,
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

        List<StarDto> starDtos;
        if (pageable != null) {
            starDtos = starsRepository.findAll(specification, pageable)
                    .getContent()
                    .stream()
                    .map(starMapper::mapToDto)
                    .toList();
        } else {
            starDtos = starsRepository.findAll(specification)
                    .stream()
                    .map(starMapper::mapToDto)
                    .toList();
        }

        for (StarDto starDto : starDtos) {
            if (starCache.get(starDto.getId()) == null) {
                starCache.put(starDto.getId(), starDto);
                log.info("Star with id {} was added to cache (getStarsByCriteria)", starDto.getId());
            } else {
                log.info("Star with id {} was already in the cache (getStarsByCriteria)", starDto.getId());
            }
        }
        return starDtos;
    }

    /* UPDATE */

    @Transactional
    public StarDto putStar(int id, StarDto starDto) {
        if (starsRepository.findById(id).isEmpty()) {
            throw new ResourceNotFoundException("Star with id " + id + " was not found for updating(put)");
        }
        log.info("Updating(put) star with id {}", id);
        Star starToPut = starMapper.mapToEntity(starDto);
        starToPut.setId(id);
        Star updatedStar = starsRepository.save(starToPut);
        StarDto updatedStarDto = starMapper.mapToDto(updatedStar);
        starCache.put(id, updatedStarDto);
        log.info("Star with id {} was updated(put) and cache was refreshed", id);
        return updatedStarDto;
    }

    @Transactional
    public StarDto patchStar(int id, StarDto starDto) {
        Star starToPatch = starsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No star with id " + id + " was found for updating(patch)"));
        log.info("Updating(patch) star with id {}", id);
        if (starDto.getName() != null) {
            starToPatch.setName(starDto.getName());
        }
        if (starDto.getType() != null) {
            starToPatch.setType(starDto.getType());
        }
        if (starDto.getMass() != null) {
            starToPatch.setMass(starDto.getMass());
        }
        if (starDto.getRadius() != null) {
            starToPatch.setRadius(starDto.getRadius());
        }
        if (starDto.getTemperature() != null) {
            starToPatch.setTemperature(starDto.getTemperature());
        }
        if (starDto.getLuminosity() != null) {
            starToPatch.setLuminosity(starDto.getLuminosity());
        }
        if (starDto.getRightAscension() != null) {
            starToPatch.setRightAscension(starDto.getRightAscension());
        }
        if (starDto.getDeclination() != null) {
            starToPatch.setDeclination(starDto.getDeclination());
        }
        if (starDto.getPositionInConstellation() != null) {
            starToPatch.setPositionInConstellation(starDto.getPositionInConstellation());
        }
        Star patchedStar = starsRepository.save(starToPatch);
        StarDto patchedStarDto = starMapper.mapToDto(patchedStar);
        starCache.put(id, patchedStarDto);
        log.info("Star with id {} was updated(patch) and cache was refreshed", id);
        return patchedStarDto;
    }

    /* DELETE */

    @Transactional
    public void deleteStar(int id) {
        Star star = starsRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("No star with id " + id + " was found for delete"));
        starsRepository.delete(star);
        starCache.remove(id);
        log.info("Star with id {} was deleted and removed from cache", id);
    }
}
