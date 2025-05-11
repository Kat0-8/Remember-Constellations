package com.example.rememberconstellations.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    private String name;
    @NotBlank
    private String type;
    @NotNull
    private Double mass;
    @NotNull
    private Double radius;
    @NotNull
    private Double temperature;
    @NotNull
    private Double luminosity;
    @NotNull
    private Double rightAscension;
    @NotNull
    private Double declination;
    private String positionInConstellation;
    private String imageUrl;
    private Integer constellationId;
}
