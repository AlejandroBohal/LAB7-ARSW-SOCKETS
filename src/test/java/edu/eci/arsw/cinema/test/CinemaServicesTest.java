package edu.eci.arsw.cinema.test;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.services.CinemaServices;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

/**
 * The type Cinema services test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration

public class CinemaServicesTest {
    /**
     * The Cinema services.
     */
    @Autowired
    CinemaServices cinemaServices;

    /**
     * Set up.
     */
    @Before
    public void setUp(){
        Cinema cinemaW = getCinemaW();
        Cinema cinemaG = getCinemaG();
        cinemaServices.addNewCinema(cinemaW);
        cinemaServices.addNewCinema(cinemaG);

    }

    /**
     * Should get all cinemas.
     */
    @Test
    public void shouldGetAllCinemas(){
        try {
            cinemaServices.getAllCinemas();
            Set<Cinema> cinemas = cinemaServices.getAllCinemas();
            Assert.assertFalse(cinemas.isEmpty());
            Assert.assertEquals(5, cinemas.size()); //3 de stub y 2 nuevos
            Cinema c = (Cinema) cinemas.toArray()[0];
            Assert.assertTrue(c.getName().contains("cinema"));

        } catch (CinemaPersistenceException e) {
            Assert.fail();
        }
    }

    /**
     * Should get cinema by name.
     *
     * @throws CinemaException            the cinema exception
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    @Test
    public void shouldGetCinemaByName() throws CinemaException, CinemaPersistenceException {
        List<CinemaFunction> cinemaList = cinemaServices.getCinemaByName("cinemaW").getFunctions();
        Assert.assertEquals(cinemaList.size(),2);
        Cinema cinema = cinemaServices.getCinemaByName("cinemaW");
        Assert.assertEquals(cinema.getName(),"cinemaW");
        try {
            cinemaServices.getCinemaByName("noFunciona");
        } catch (CinemaException e) {
            e.printStackTrace();
        } catch (CinemaPersistenceException e) {
            Assert.assertTrue(true);
        }
    }

    /**
     * Should build ticket.
     *
     * @throws CinemaException            the cinema exception
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    @Test
    public void shouldBuildTicket() throws CinemaException, CinemaPersistenceException {
        cinemaServices.buyTicket(0,0,"cinemaW","2020-12-20 15:30","ProbandoAndo");
        cinemaServices.buyTicket(3,4,"cinemaW","2020-12-20 15:30","ProbandoAndo");
        Cinema cinema =cinemaServices.getCinemaByName("cinemaW");
        cinema.getFunctions().forEach(cinemaFunction -> {
            if (cinemaFunction.getMovie().getName().equals("ProbandoAndo")){
                List<List<Boolean>> seats = cinemaFunction.getSeats();
                Assert.assertEquals(seats.get(0).get(0),false);
                Assert.assertEquals(seats.get(3).get(4),false);
                Assert.assertEquals(seats.get(4).get(4),true);
            }
        });
    }

    /**
     * Should get functions by cinema and date.
     *
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    @Test
    public void shouldGetFunctionsByCinemaAndDate() throws CinemaPersistenceException {
        List<CinemaFunction> functions = cinemaServices.getFunctionsByCinemaAndDate("cinemaW","2020-12-20");
        Assert.assertEquals(functions.size(),2);
        for(CinemaFunction function : functions){
            if (function.getMovie().getName().equals("PeliPrueba")){
                Assert.assertEquals("Action", function.getMovie().getGenre());
            }
            if (function.getMovie().getName().equals("ProbandoAndo")){
                Assert.assertEquals("Horror", function.getMovie().getGenre());
            }
        }
        try{
            cinemaServices.getFunctionsByCinemaAndDate("cinemaO","2020-12-20");
            Assert.fail("No debería encontrar");
        }catch (Exception e){
            Assert.assertTrue(true);
        }
    }

    /**
     * Should get functions by cinema and date and movie.
     *
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    @Test
    public void shouldGetFunctionsByCinemaAndDateAndMovie() throws CinemaPersistenceException {
        CinemaFunction function = cinemaServices.getFunctionByCinemaAndDateAndMovie(
                "cinemaG","2020-11-20 15:30","Prueba2");
        Assert.assertNotNull(function);
        Assert.assertEquals(function.getDate(),"2020-11-20 15:30");
        Assert.assertEquals(function.getMovie().getName(),"Prueba2");
    }

    /**
     * Should add function to cinema.
     *
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    @Test
    public void shouldAddFunctionToCinema() throws CinemaPersistenceException {
        CinemaFunction funct1 = new CinemaFunction(new Movie("UltimaPelicula","Drama"),"2030-11-20 15:30");
        cinemaServices.addFunctionToCinema("cinemaG",funct1);
        List<CinemaFunction> cinemaFunctions = cinemaServices.getFunctionsByCinemaAndDate("cinemaG","2030-11-20 15:30");
        Assert.assertEquals(cinemaFunctions.size(),1);
        try{
            cinemaServices.addFunctionToCinema("cinemaG",funct1);
        }catch (CinemaPersistenceException e){
            Assert.assertTrue(true); //Excepcion si llegan iguales.
        }
        Assert.assertEquals(cinemaFunctions.size(),1);
    }

    /**
     * Should update function by cinema.
     *
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    @Test
    public void shouldUpdateFunctionByCinema() throws CinemaPersistenceException {
        CinemaFunction funct1 = new CinemaFunction(new Movie("ProbandoAndo2","Comedia"),"2020-11-20 15:30");
        CinemaFunction funct2 = new CinemaFunction(new Movie("Prueba2","Comedia"),"2021-11-20 15:30");
        CinemaFunction actual = cinemaServices.getFunctionByCinemaAndDateAndMovie(
                "cinemaG","2020-11-20","ProbandoAndo2");
        Assert.assertEquals(actual.getMovie().getGenre(),"Horror");
        cinemaServices.updateFunctionByCinema("cinemaG",funct1);
        Assert.assertEquals(actual.getMovie().getGenre(),"Comedia");
        //Test2
        CinemaFunction actual2 = cinemaServices.getFunctionByCinemaAndDateAndMovie(
                "cinemaG","2020-11-20","Prueba2");
        Assert.assertEquals(actual2.getMovie().getGenre(),"Action");
        Assert.assertEquals(actual2.getDate(),"2020-11-20 15:30");
        cinemaServices.updateFunctionByCinema("cinemaG",funct2);
        Assert.assertEquals(actual2.getMovie().getGenre(),"Comedia");
        Assert.assertEquals(actual2.getDate(),"2021-11-20 15:30");

    }
    private Cinema getCinemaW() {
        String functionDate2 = "2020-12-20 15:30";
        List<CinemaFunction> functions= new ArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("PeliPrueba","Action"),functionDate2);
        CinemaFunction funct2 = new CinemaFunction(new Movie("ProbandoAndo","Horror"),functionDate2);
        functions.add(funct1);
        functions.add(funct2);
        return new Cinema("cinemaW",functions);
    }
    private Cinema getCinemaG() {
        String functionDate2 = "2020-11-20 15:30";
        List<CinemaFunction> functions= new ArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("Prueba2","Action"),functionDate2);
        CinemaFunction funct2 = new CinemaFunction(new Movie("ProbandoAndo2","Horror"),functionDate2);
        functions.add(funct1);
        functions.add(funct2);
        return new Cinema("cinemaG",functions);
    }

}
