package com.example.rememberconstellations.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ConstellationDto {

    private int id;
    private String name;
    private String abbreviation;
    private String family;
    private String region;
    private List<StarDto> stars;
}
