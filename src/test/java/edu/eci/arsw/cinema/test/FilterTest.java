package edu.eci.arsw.cinema.test;

import edu.eci.arsw.cinema.filter.CinemaFilterException;
import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import java.util.List;

import edu.eci.arsw.cinema.services.CinemaServices;
import org.junit.Test;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@RunWith( SpringRunner.class )
@SpringBootTest
public class FilterTest {
    @Autowired
    private CinemaServices cs;
    private Cinema c;
    @Before
    public void setUp() throws CinemaException, CinemaPersistenceException {
        c = cs.getCinemaByName("cinemaX");
    }

    @Test
    public void shouldFilterByAvailability()  {
        List<Movie> f = null;

        try {
            f = cs.filter("cinemaX","2018-12-18 15:30","80");
        } catch (CinemaException | CinemaPersistenceException | CinemaFilterException e) {
            fail();
        }

        String[] answers = new String[]{"SuperHeroes Movie","The Night"};
        for (int i = 0; i < f.size(); i++) {
            assertEquals(answers[i],f.get(i).getName());
        }
    }

}
