package com.example.rememberconstellations.repositories;

import com.example.rememberconstellations.models.Star;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface StarsRepository extends JpaRepository<Star, Integer>, JpaSpecificationExecutor<Star> {

    @EntityGraph(attributePaths = {"constellation"})
    Optional<Star> findById(int id);

    boolean existsByName(String name);
}
