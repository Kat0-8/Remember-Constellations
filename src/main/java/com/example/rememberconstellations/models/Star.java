package com.example.rememberconstellations.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "stars")
public class Star {
    private static final String UNDEFINED = "UNDEFINED";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "star_id", nullable = false)
    private int id;

    @Column(name = "star_name", nullable = false)
    private String name;

    @Column(name = "star_type", nullable = false)
    private String type;

    @Column(name = "star_mass", nullable = false)
    private double mass;

    @Column(name = "star_radius", nullable = false)
    private double radius;

    @Column(name = "star_temperature", nullable = false)
    private double temperature;

    @Column(name = "star_luminosity", nullable = false)
    private double luminosity;

    @Column(name = "star_right_ascension", nullable = false)
    private double rightAscension;

    @Column(name = "star_declination", nullable = false)
    private double declination;

    @Column(name = "star_position_in_constellation", nullable = false)
    private String positionInConstellation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "constellation_id")
    @JsonBackReference
    private Constellation constellation;

    public Star(String name, String type, double mass, double radius,
                double temperature, double luminosity, double rightAscension,
                double declination, String positionInConstellation) {
        this.name = name;
        this.type = type;
        this.mass = mass;
        this.radius = radius;
        this.temperature = temperature;
        this.luminosity = luminosity;
        this.rightAscension = rightAscension;
        this.declination = declination;
        this.positionInConstellation = positionInConstellation;
    }

    public Star() {
        this.id = -1;
        this.name = UNDEFINED;
        this.type = UNDEFINED;
        this.mass = 0.0;
        this.radius = 0.0;
        this.temperature = 0.0;
        this.luminosity = 0.0;
        this.rightAscension = 0.0;
        this.declination = 0.0;
        this.positionInConstellation = UNDEFINED;
    }
}
