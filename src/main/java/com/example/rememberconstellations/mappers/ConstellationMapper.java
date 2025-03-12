package com.example.rememberconstellations.mappers;

import com.example.rememberconstellations.dto.ConstellationDto;
import com.example.rememberconstellations.dto.StarDto;
import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class ConstellationMapper {

    public ConstellationDto mapToDto(Constellation constellation) {
        List<StarDto> starDtos = constellation.getStars().stream()
                .map(star -> new StarDto(star.getId(), star.getName(), star.getType(), star.getMass(),
                        star.getRadius(), star.getTemperature(), star.getLuminosity(),
                        star.getRightAscension(), star.getDeclination(), star.getPositionInConstellation()))
                .collect(Collectors.toList());

        return new ConstellationDto(constellation.getId(), constellation.getName(), constellation.getAbbreviation(),
                constellation.getFamily(), constellation.getRegion(), starDtos);
    }

    public Constellation mapToEntity(ConstellationDto constellationDto) {
        Constellation constellation = new Constellation();
        constellation.setId(constellationDto.getId());
        constellation.setName(constellationDto.getName());
        constellation.setAbbreviation(constellationDto.getAbbreviation());
        constellation.setFamily(constellationDto.getFamily());
        constellation.setRegion(constellationDto.getRegion());
        List<Star> stars = constellationDto.getStars().stream()
                .map(starDto -> {
                    Star star = new Star();
                    star.setId(starDto.getId());
                    star.setName(starDto.getName());
                    star.setType(starDto.getType());
                    star.setMass(starDto.getMass());
                    star.setRadius(starDto.getRadius());
                    star.setTemperature(starDto.getTemperature());
                    star.setLuminosity(starDto.getLuminosity());
                    star.setRightAscension(starDto.getRightAscension());
                    star.setDeclination(starDto.getDeclination());
                    star.setPositionInConstellation(starDto.getPositionInConstellation());
                    star.setConstellation(constellation);
                    return star;
                })
                .collect(Collectors.toList());
        constellation.setStars(stars);

        return constellation;
    }
}
