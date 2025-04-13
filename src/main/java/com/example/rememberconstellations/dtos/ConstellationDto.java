package com.example.rememberconstellations.dtos;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String name;
    @NotBlank
    private String abbreviation;
    @NotBlank
    private String family;
    @NotBlank
    private String region;
    private List<StarDto> stars;
}
