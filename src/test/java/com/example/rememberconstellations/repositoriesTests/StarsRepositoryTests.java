package com.example.rememberconstellations.repositoriesTests;

//import com.example.rememberconstellations.models.Star;
//import com.example.rememberconstellations.models.Constellation;
//import com.example.rememberconstellations.repositories.StarsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class StarsRepositoryTests {

    /*
    @Autowired
    private StarsRepository starsRepository;
     */
    // private Star star;
    // private Constellation constellation;

    @BeforeEach
    void setUp() {
        //nested
    }

    @Test
    void testFindStarByIdShouldReturnStarWithConstellation() {
        //nested
    }

    @Test
    void testFindStarByIdShouldReturnEmptyWhenStarNotFound() {
        //nested
    }
}
