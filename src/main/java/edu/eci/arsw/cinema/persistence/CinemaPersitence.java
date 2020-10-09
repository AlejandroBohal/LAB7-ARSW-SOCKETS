/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.persistence;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import java.util.List;
import java.util.Set;

/**
 * The interface Cinema persitence.
 *
 * @author cristian
 */
public interface CinemaPersitence {

    /**
     * Buy ticket.
     *
     * @param row       the row of the seat
     * @param col       the column of the seat
     * @param cinema    the cinema's name
     * @param date      the date of the function
     * @param movieName the name of the movie
     * @throws CinemaException if the seat is occupied,    or any other low-level persistence error occurs.
     */
    public CinemaFunction buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException;

    /**
     * Gets functionsby cinema and date.
     *
     * @param cinema cinema's name
     * @param date   date
     * @return the list of the functions of the cinema in the given date
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date) ;

    /**
     * Save cinema.
     *
     * @param cinema new cinema
     * @throws CinemaPersistenceException n if a cinema with the same name already exists
     */
    public void saveCinema(Cinema cinema) throws CinemaPersistenceException;

    /**
     * Gets cinema.
     *
     * @param name name of the cinema
     * @return Cinema of the given name
     * @throws CinemaPersistenceException if there is no such cinema
     */
    public Cinema getCinema(String name) throws CinemaPersistenceException;

    /**
     * Gets all cinemas.
     *
     * @return the all cinemas
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    public Set<Cinema> getAllCinemas() throws CinemaPersistenceException;

    /**
     * Gets functionby cinema and date and movie.
     *
     * @param name      the name
     * @param date      the date
     * @param moviename the moviename
     * @return the functionby cinema and date and movie
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    CinemaFunction getFunctionbyCinemaAndDateAndMovie(String name, String date, String moviename) throws CinemaPersistenceException;

    /**
     * Add function to cinema cinema function.
     *
     * @param name     the name
     * @param function the function
     * @return the cinema function
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    CinemaFunction addFunctionToCinema(String name, CinemaFunction function) throws CinemaPersistenceException;

    /**
     * Update function by cinema cinema function.
     *
     * @param name     the name
     * @param function the function
     * @return the cinema function
     * @throws CinemaPersistenceException the cinema persistence exception
     */
    CinemaFunction updateFunctionByCinema(String name, CinemaFunction function) throws  CinemaPersistenceException;

    void addCinema(Cinema c);
    void deleteFunctionByCinemaDateAndMovieName(String name,String date,String moviename) throws CinemaPersistenceException;

}
