package edu.eci.arsw.cinema.test;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
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
public class PersistenceTest {
    @Autowired
    private CinemaServices cinemaServices;
    private Cinema c;
    private List<CinemaFunction> cfs;
    @Before
    public void setUp() throws CinemaException, CinemaPersistenceException {
        c = cinemaServices.getCinemaByName("cinemaX");
        cfs = cinemaServices.getFunctionsByCinemaAndDate("cinemaX", "2018-12-18 15:30");
    }
    @Test
    public void shouldReturnCinemaName(){
        String name = c.getName();
        assertEquals("cinemaX",name);
    }

    @Test
    public void shouldReturnByCinemaAndDate(){
        String[] answers = new String[]{"SuperHeroes Movie","The Night"};
        for (int i = 0; i < cfs.size(); i++) {
            assertEquals(answers[i],cfs.get(i).getMovie().getName());
        }
    }
    @Test
    public void shouldBuyTickets(){
        int before = cfs.get(0).movieAvailability(); //SuperHeroes Movie
        try{
            cinemaServices.buyTicket(1,1,"cinemaX","2018-12-18 15:30","SuperHeroes Movie");
        }
        catch (CinemaException e){
            fail();
        }
        int after = cfs.get(0).movieAvailability();
        assertTrue(before > after);

    }

}
