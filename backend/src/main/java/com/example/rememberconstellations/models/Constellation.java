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
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @NotBlank
    @Column(name = "abbreviation", nullable = false)
    private String abbreviation;

    @NotBlank
    @Column(name = "family", nullable = false)
    private String family;

    @NotBlank
    @Column(name = "region", nullable = false)
    private String region;

    @Column(name = "image_url", nullable = true)
    private String imageUrl;

    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonManagedReference
    @OneToMany(mappedBy = "constellation", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    @SuppressWarnings("java:S7027")
    private List<Star> stars = new ArrayList<>();
}
