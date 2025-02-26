package com.example.rememberconstellations.models;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Constellation {
    private String name;
    private String abbreviation;
    private List<Star> stars;

    public Constellation(String name, String abbreviation, List<Star> stars) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.stars = stars;
    }

}
