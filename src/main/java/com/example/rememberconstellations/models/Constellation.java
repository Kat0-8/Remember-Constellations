package com.example.rememberconstellations.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table(name = "constellations")
public class Constellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "constellation_id", nullable = false)
    private int id;

    @Column(name = "constellation_name", nullable = false)
    private String name;

    @Column(name = "constellation_abbreviation", nullable = false)
    private String abbreviation;

    @Column(name = "constellation_family", nullable = false)
    private String family;

    @Column(name = "constellation_region", nullable = false)
    private String region;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonManagedReference
    @OneToMany(mappedBy = "constellation", fetch = FetchType.LAZY,
            cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = false)
    private List<Star> stars = new ArrayList<>();

    public Constellation(int id, String name, String abbreviation,
                         String family, String region, List<Star> stars) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.family = family;
        this.region = region;
        this.stars = stars;
    }
}
