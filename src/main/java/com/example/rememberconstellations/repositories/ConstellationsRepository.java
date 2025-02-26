package com.example.rememberconstellations.repositories;

import com.example.rememberconstellations.models.Constellation;
import com.example.rememberconstellations.models.Star;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class ConstellationsRepository {

    private final List<Constellation> constellations = new ArrayList<>();

    private static final String SUPERGIANT = "Supergiant";
    private static final String GIANT = "Giant";

    public ConstellationsRepository() {
        constellations.add(new Constellation("Orion", "Ori", List.of(
                new Star("Rigel", SUPERGIANT, 18, 79,
                        12130, 120000, "Beta"),
                new Star("Betelgeuse", SUPERGIANT, 16.5, 764,
                        3600, 126000, "Alpha"),
                new Star("Bellatrix", GIANT, 8.4, 6,
                        22000, 6400, "Gamma"),
                new Star("Alnilam", SUPERGIANT, 40, 32.4,
                        27500, 537000, "Epsilon"),
                new Star("Alnitak", SUPERGIANT, 18, 20,
                        33500, 35000, "Zeta"),
                new Star("Saiph", SUPERGIANT, 15.5, 22.2,
                        26500, 56881, "Kappa"),
                new Star("Mintaka", GIANT, 17.8, 13.1,
                        18400, 190000, "Delta"),
                new Star("Hatsya", "Variable", 23.1, 8.3,
                        32500, 68000, "Eta")
        )));
        constellations.add(new Constellation("Ursa Major", "UMa", List.of(
                new Star("Alkaid", GIANT, 6.1, 3.4,
                        15540, 594, "Eta"),
                new Star("Dubhe", GIANT, 5, 30,
                        4685, 360.835, "Alpha")
        )));
    }

    public List<Constellation> getAllConstellations() {
        return List.copyOf(constellations);
    }

    public Constellation getConstellationByName(String name) {
        return constellations.stream()
                .filter(constellation -> constellation.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public Constellation getConstellationByAbbreviation(String abbreviation) {
        return constellations.stream()
                .filter(constellation -> constellation.getAbbreviation().equalsIgnoreCase(abbreviation))
                .findFirst()
                .orElse(null);
    }
}
