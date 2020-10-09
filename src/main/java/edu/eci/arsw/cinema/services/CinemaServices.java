/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.services;

import edu.eci.arsw.cinema.filter.CinemaFilterException;
import edu.eci.arsw.cinema.filter.CinemaFilterI;
import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author cristian
 */
@Service
public class CinemaServices {
    @Autowired
    @Qualifier("inMemoryCP")
    CinemaPersitence cps;
    @Autowired
    @Qualifier("FilterByA")
    CinemaFilterI cf;

    public void addNewCinema(Cinema c){
        cps.addCinema(c);
    }

    public Set<Cinema> getAllCinemas() throws CinemaPersistenceException {

        return cps.getAllCinemas();
    }
    
    /**
     * 
     * @param name cinema's name
     * @return the cinema of the given name created by the given author
     * @throws CinemaException
     */
    public Cinema getCinemaByName(String name) throws CinemaException, CinemaPersistenceException {
        return cps.getCinema(name);
    }
    
    
    public CinemaFunction buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException {
        return cps.buyTicket(row,col,cinema,date,movieName);
    }
    
    public List<CinemaFunction> getFunctionsByCinemaAndDate(String cinema, String date){
        String onlyDate = date.split(" ")[0];
        return cps.getFunctionsbyCinemaAndDate(cinema,onlyDate);
    }


    public List<Movie> filter(String cinema, String date, String filter) throws CinemaException, CinemaPersistenceException, CinemaFilterException {
        Cinema cinemA = this.getCinemaByName(cinema);
        return cf.filerMovie(cinemA,date,filter);
    }

    public CinemaFunction getFunctionByCinemaAndDateAndMovie(String name, String date, String moviename) throws CinemaPersistenceException{
        return cps.getFunctionbyCinemaAndDateAndMovie(name,date,moviename);
    }

    public CinemaFunction addFunctionToCinema(String name, CinemaFunction function) throws CinemaPersistenceException{
        return cps.addFunctionToCinema(name,function);
    }

    public CinemaFunction updateFunctionByCinema(String name, CinemaFunction function) throws CinemaPersistenceException{
        return cps.updateFunctionByCinema(name,function);
    }
    public void deleteFunctionByCinemaDateAndMovieName(String name, String date, String movieName) throws CinemaPersistenceException{
        cps.deleteFunctionByCinemaDateAndMovieName(name,date,movieName);
    }
}
