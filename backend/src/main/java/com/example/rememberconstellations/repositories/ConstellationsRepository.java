package com.example.rememberconstellations.repositories;

import com.example.rememberconstellations.models.Constellation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConstellationsRepository extends JpaRepository<Constellation, Integer>, JpaSpecificationExecutor<Constellation> {

    @EntityGraph(attributePaths = {"stars"})
    Optional<Constellation> findById(int id);

    /*
    @Query("SELECT * FROM constellations const " +
           "JOIN stars ss ON const.id = ss.constellation_id " +
           "WHERE ss.type = :type", nativeQuery = true)
    */
    @Query("SELECT const FROM Constellation const JOIN const.stars ss WHERE ss.type = :type")
    List<Constellation> findByStarType(@Param("type") String starType);

    boolean existsByName(String name);
}
