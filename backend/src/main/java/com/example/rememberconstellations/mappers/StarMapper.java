package com.example.rememberconstellations.mappers;

import com.example.rememberconstellations.dtos.StarDto;
import com.example.rememberconstellations.models.Star;
import org.springframework.stereotype.Component;

@Component
public class StarMapper {

    public StarDto mapToDto(Star star) {
        StarDto starDto = new StarDto();
        starDto.setId(star.getId());
        starDto.setName(star.getName());
        starDto.setType(star.getType());
        starDto.setMass(star.getMass());
        starDto.setRadius(star.getRadius());
        starDto.setTemperature(star.getTemperature());
        starDto.setLuminosity(star.getLuminosity());
        starDto.setRightAscension(star.getRightAscension());
        starDto.setDeclination(star.getDeclination());
        starDto.setPositionInConstellation(star.getPositionInConstellation());
        starDto.setImageUrl(star.getImageUrl());
        if (star.getConstellation() != null) {
            starDto.setConstellationId(star.getConstellation().getId());
        } else {
            starDto.setConstellationId(null);
        }
        return starDto;
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
        star.setImageUrl(starDto.getImageUrl());
        return star;
    }
}
