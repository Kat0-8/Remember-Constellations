package com.example.rememberconstellations.models;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Star {
    private String name;
    private String type;
    private double mass;
    private double radius;
    private double temperature;
    private double luminosity;
    private String positionInConstellation;  // Позиция звезды в созвездии

    public Star(String name, String type, double mass, double radius,
                double temperature, double luminosity, String positionInConstellation) {
        this.name = name;
        this.type = type;
        this.mass = mass;
        this.radius = radius;
        this.temperature = temperature;
        this.luminosity = luminosity;
        this.positionInConstellation = positionInConstellation;
    }

    public Star() {
        this.name = "UNDEFINED";
        this.type = "UNDEFINED";
        this.mass = 0.0;
        this.radius = 0.0;
        this.temperature = 0.0;
        this.luminosity = 0.0;
        this.positionInConstellation = "UNDEFINED";
    }

}
