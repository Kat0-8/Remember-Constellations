package com.example.rememberconstellations.mappers;

import com.example.rememberconstellations.dto.StarDto;
import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import org.springframework.stereotype.Component;

@Component
public class StarMapper {

    public StarDto mapToDto(Star star) {
        return new StarDto(
                star.getId(),
                star.getName(),
                star.getType(),
                star.getMass(),
                star.getRadius(),
                star.getTemperature(),
                star.getLuminosity(),
                star.getRightAscension(),
                star.getDeclination(),
                star.getPositionInConstellation(),
                star.getConstellation().getId()
        );
    }

    public Star mapToEntity(StarDto starDto) {
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

        Constellation constellation = new Constellation();
        constellation.setId(starDto.getConstellationId());
        star.setConstellation(constellation);
        return star;
    }
}
