package com.example.rememberconstellations.mappers;

import com.example.rememberconstellations.dtos.ConstellationDto;
import com.example.rememberconstellations.dtos.StarDto;
import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConstellationMapper {

    private final StarMapper starMapper;

    @Autowired
    public ConstellationMapper(StarMapper starMapper) {
        this.starMapper = starMapper;
    }

    public ConstellationDto mapToDto(Constellation constellation) {
        List<StarDto> starDtos = constellation.getStars().stream()
                .map(starMapper::mapToDto)
                .collect(Collectors.toList());

        return new ConstellationDto(constellation.getId(), constellation.getName(), constellation.getAbbreviation(),
                constellation.getFamily(), constellation.getRegion(), constellation.getImageUrl(), starDtos);
    }

    public Constellation mapToEntity(ConstellationDto constellationDto) {
        Constellation constellation = new Constellation();
        constellation.setId(constellationDto.getId());
        constellation.setName(constellationDto.getName());
        constellation.setAbbreviation(constellationDto.getAbbreviation());
        constellation.setFamily(constellationDto.getFamily());
        constellation.setRegion(constellationDto.getRegion());
        constellation.setImageUrl(constellationDto.getImageUrl());
        List<Star> stars = constellationDto.getStars().stream()
                .map(starDto -> {
                    Star star = starMapper.mapToEntity(starDto);
                    star.setConstellation(constellation);
                    return star;
                })
                .collect(Collectors.toList());
        constellation.setStars(stars);

        return constellation;
    }
}
