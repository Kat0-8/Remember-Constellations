package com.example.rememberconstellations.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stars")
public class Star {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "mass", nullable = false)
    private Double mass;

    @Column(name = "radius", nullable = false)
    private Double radius;

    @Column(name = "temperature", nullable = false)
    private Double temperature;

    @Column(name = "luminosity", nullable = false)
    private Double luminosity;

    @Column(name = "right_ascension", nullable = false)
    private Double rightAscension;

    @Column(name = "declination", nullable = false)
    private Double declination;

    @Column(name = "position_in_constellation", nullable = true)
    private String positionInConstellation;

    @JsonIdentityReference(alwaysAsId = true)
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "constellation_id", nullable = true)
    private Constellation constellation;

    @SuppressWarnings("java:S107")
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
}
