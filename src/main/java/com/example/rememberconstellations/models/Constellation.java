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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "constellations")
public class Constellation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "abbreviation", nullable = false)
    private String abbreviation;

    @Column(name = "family", nullable = false)
    private String family;

    @Column(name = "region", nullable = false)
    private String region;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonManagedReference
    @OneToMany(mappedBy = "constellation", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Star> stars = new ArrayList<>();
}
