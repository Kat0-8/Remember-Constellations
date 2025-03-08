package com.example.rememberconstellations.repositoriesTests;

//import com.example.rememberconstellations.models.Constellation;
//import com.example.rememberconstellations.repositories.ConstellationsRepository;
//import com.example.rememberconstellations.repositories.StarsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class ConstellationsRepositoryTest {

    /*

    @Autowired
    private ConstellationsRepository constellationsRepository;

    @Autowired
    private StarsRepository starsRepository;

     */

  //private Constellation constellation;

    @BeforeEach
    void setUp() {
        //nested
    }

    @Test
    void testFindByIdWithStars() {
       //nested
    }

    @Test
    void testFindByIdWithNoStars() {
        //nested
    }
}

