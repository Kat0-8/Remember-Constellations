package com.example.rememberconstellations.services;

import com.example.rememberconstellations.cache.StarCache;
import com.example.rememberconstellations.dto.StarDto;
import com.example.rememberconstellations.mappers.StarMapper;
import com.example.rememberconstellations.models.Star;
import com.example.rememberconstellations.repositories.StarsRepository;
import com.example.rememberconstellations.utilities.StarSpecification;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Star star = starMapper.mapToEntity(starDto);
        Star savedStar = starsRepository.save(star);
        StarDto savedStarDto = starMapper.mapToDto(savedStar);
        starCache.put(savedStar.getId(), savedStarDto);
        return savedStarDto;
    }

    /* READ */

    public Optional<StarDto> getStarById(final int id) {
        StarDto cashedStarDto = starCache.get(id);
        if (cashedStarDto != null) {
            return Optional.of(cashedStarDto);
        }
        return starsRepository.findStarById(id)
                .map(star -> {
                    StarDto starDto = starMapper.mapToDto(star);
                    starCache.put(id, starDto);
                    return starDto;
                });
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
            }
        }
        return starDtos;
    }

    /* UPDATE */

    @Transactional
    public Optional<StarDto> putStar(int id, StarDto starDto) {
        if (starsRepository.existsById(id)) {
            Star star = starMapper.mapToEntity(starDto);
            star.setId(id);
            Star updatedStar = starsRepository.save(star);
            StarDto updatedStarDto = starMapper.mapToDto(updatedStar);
            starCache.put(id, updatedStarDto);
            return Optional.of(updatedStarDto);
        } else {
            return Optional.empty();
        }
    }

    @Transactional
    public Optional<StarDto> patchStar(int id, StarDto starDto) {
        Star star;
        Optional<Star> starToPatch = starsRepository.findStarById(id);
        if (starToPatch.isPresent()) {
            star = starToPatch.get();

            if (starDto.getName() != null) {
                star.setName(starDto.getName());
            }
            if (starDto.getType() != null) {
                star.setType(starDto.getType());
            }
            if (starDto.getMass() != null) {
                star.setMass(starDto.getMass());
            }
            if (starDto.getRadius() != null) {
                star.setRadius(starDto.getRadius());
            }
            if (starDto.getTemperature() != null) {
                star.setTemperature(starDto.getTemperature());
            }
            if (starDto.getLuminosity() != null) {
                star.setLuminosity(starDto.getLuminosity());
            }
            if (starDto.getRightAscension() != null) {
                star.setRightAscension(starDto.getRightAscension());
            }
            if (starDto.getDeclination() != null) {
                star.setDeclination(starDto.getDeclination());
            }
            if (starDto.getPositionInConstellation() != null) {
                star.setPositionInConstellation(starDto.getPositionInConstellation());
            }
            Star patchedStar = starsRepository.save(star);
            StarDto patchedStarDto = starMapper.mapToDto(patchedStar);
            starCache.put(id, patchedStarDto);
            return Optional.of(patchedStarDto);
        } else {
            return Optional.empty();
        }
    }

    /* DELETE */

    @Transactional
    public boolean deleteStar(int id) {
        Optional<Star> starOptional = starsRepository.findStarById(id);
        if (starOptional.isPresent()) {
            starsRepository.deleteById(id);
            starCache.remove(id);
            return true;
        } else {
            return false;
        }
    }
}
