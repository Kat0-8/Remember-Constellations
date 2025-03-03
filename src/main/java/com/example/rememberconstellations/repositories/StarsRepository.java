package com.example.rememberconstellations.repositories;

import com.example.rememberconstellations.models.Star;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StarsRepository extends JpaRepository<Star, Integer> {

    Optional<Star> findStarById(int id);
}
