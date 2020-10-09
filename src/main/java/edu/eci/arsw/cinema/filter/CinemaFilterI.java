package edu.eci.arsw.cinema.filter;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.Movie;

import java.util.List;


public interface CinemaFilterI{
    public List<Movie> filerMovie(Cinema cinema, String date,String filter) throws CinemaFilterException;
}
