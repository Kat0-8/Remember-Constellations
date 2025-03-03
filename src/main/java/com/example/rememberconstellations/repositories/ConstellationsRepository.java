package com.example.rememberconstellations.repositories;

import com.example.rememberconstellations.models.Constellation;
import java.util.List;
import java.util.Optional;
//import jakarta.annotation.Nullable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstellationsRepository extends JpaRepository<Constellation, Integer>, JpaSpecificationExecutor<Constellation> {

    @EntityGraph(attributePaths = {"stars"})
    Optional<Constellation> findById(int id);
}
