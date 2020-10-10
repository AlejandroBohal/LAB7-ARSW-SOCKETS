package edu.eci.arsw.cinema.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.eci.arsw.cinema.CinemaAPIApplication;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.services.CinemaServices;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * The type Controller test.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes={ CinemaAPIApplication.class })
@AutoConfigureMockMvc

public class ControllerTest {
    @Autowired
    private MockMvc mvc;
    @Autowired
    private CinemaServices cinemaServices;

    /**
     * Should get cinemas.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldGetCinemas() throws Exception{
        mvc.perform(
                MockMvcRequestBuilders.get("/cinemas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted());
    }

    /**
     * Should get cinema by name.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldGetCinemaByName() throws Exception{
        mvc.perform(
                MockMvcRequestBuilders.get("/cinemas/{name}","cinemaX")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
                        .accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isAccepted()).andExpect(jsonPath("$.name",is("cinemaX")));
    }

    /**
     * Should add function to cinema.
     *
     * @throws Exception the exception
     */
    @Test
    public void shouldAddFunctionToCinema() throws Exception{
        CinemaFunction functionTest = new CinemaFunction(new Movie("PruebaMvc","Action"),"2020-11-20 15:30");

        mvc.perform(
                MockMvcRequestBuilders
                        .post("/cinemas/{name}","cinemaD")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(functionTest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.movie.name",is("PruebaMvc")));
    }

    /**
     * As json string string.
     *
     * @param obj the obj
     * @return the string
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
