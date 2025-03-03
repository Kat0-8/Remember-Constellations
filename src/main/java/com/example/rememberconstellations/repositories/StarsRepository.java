package com.example.rememberconstellations.repositories;

import com.example.rememberconstellations.models.Star;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface StarsRepository extends JpaRepository<Star, Integer> {
    @EntityGraph(attributePaths = {"constellation"})
    //@Query("SELECT star from stars star WHERE star.id =  :id")
    Optional<Star> findStarById(int id);
}
