package com.example.rememberconstellations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StarDto {

    private int id;
    private String name;
    private String type;
    private Double mass;
    private Double radius;
    private Double temperature;
    private Double luminosity;
    private Double rightAscension;
    private Double declination;
    private String positionInConstellation;
}
