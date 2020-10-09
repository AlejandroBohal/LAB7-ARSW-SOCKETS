/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.controllers;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.services.CinemaServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The type Cinema api controller.
 *
 * @author cristian
 */
@RestController("/")
public class CinemaAPIController {
    @Autowired
    private CinemaServices cinemaServices;

    /**
     * Controller get resource cinema response entity.
     *
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping(value = "/cinemas")
    public ResponseEntity<?> controllerGetResourceCinema() throws ResourceNotFoundException {
        try {
            Set<Cinema> data = cinemaServices.getAllCinemas();
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (CinemaPersistenceException e) {
            throw  new ResourceNotFoundException(e.getMessage(),e);
        }
    }

    /**
     * Gets cinema by name.
     *
     * @param name the name
     * @return the cinema by name
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping( value = "/cinemas/{name}")
    public ResponseEntity<?> getCinemaByName(@PathVariable String name) throws ResourceNotFoundException {
        try {
            Cinema data = cinemaServices.getCinemaByName(name);
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (CinemaException | CinemaPersistenceException e) {
            throw  new ResourceNotFoundException(e.getMessage(),e);
        }
    }

    /**
     * Gets functions by name and date.
     *
     * @param name the name
     * @param date the date
     * @return the functions by name and date
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping( value = "/cinemas/{name}/{date}")
    public ResponseEntity<?> getFunctionsByNameAndDate(@PathVariable String name,@PathVariable String date){
        try {
            List<CinemaFunction> data = cinemaServices.getFunctionsByCinemaAndDate(name,date);
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets functions by name and date and movie.
     *
     * @param name      the name
     * @param date      the date
     * @param moviename the moviename
     * @return the functions by name and date and movie
     * @throws ResourceNotFoundException the resource not found exception
     */
    @GetMapping( value = "/cinemas/{name}/{date}/{moviename}")
    public ResponseEntity<?> getFunctionsByNameAndDateAndMovie(@PathVariable String name,@PathVariable String date,@PathVariable String moviename) throws ResourceNotFoundException {
        try {
            CinemaFunction data = cinemaServices.getFunctionByCinemaAndDateAndMovie(name,date,moviename);
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (CinemaPersistenceException e) {
            throw  new ResourceNotFoundException(e.getMessage(),e);
        }
    }

    /**
     * Buy ticket response entity.
     *
     * @param name      the name
     * @param date      the date
     * @param moviename the moviename
     * @param row       the row
     * @param col       the col
     * @return the response entity
     * @throws ResourceNotFoundException the resource not found exception
     */
    @PostMapping( value = "/cinemas/{name}/{date}/{moviename}/{row}/{col}")
    public ResponseEntity<?> buyTicket(@PathVariable String name,@PathVariable String date,@PathVariable String moviename, @PathVariable int row, @PathVariable int col) throws ResourceNotFoundException {
        try {
            cinemaServices.buyTicket(row,col,name,date,moviename);
            CinemaFunction data = cinemaServices.getFunctionByCinemaAndDateAndMovie(name,date,moviename);
            return new ResponseEntity<>(data, HttpStatus.ACCEPTED);
        } catch (CinemaPersistenceException | CinemaException e) {
            throw  new ResourceNotFoundException(e.getMessage(),e);
        }
    }

    /**
     * Add function to cinema response entity.
     *
     * @param name     the name
     * @param function the function
     * @return the response entity
     */
    @PostMapping(value = "/cinemas/{name}")
    public ResponseEntity<?> addFunctionToCinema(@PathVariable String name,@RequestBody CinemaFunction function) {
        try{
            CinemaFunction data = cinemaServices.addFunctionToCinema(name,function);
            return  new ResponseEntity<>(data,HttpStatus.CREATED);
        }catch (CinemaPersistenceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
    /**
     * Update function by cinema response entity.
     *
     * @param name     the name
     * @param function the function
     * @return the response entity
     */
    @PutMapping(value = "/cinemas/{name}")
    public ResponseEntity<?> updateFunctionByCinema(@PathVariable String name,@RequestBody CinemaFunction function) {
        try{
            CinemaFunction data = cinemaServices.updateFunctionByCinema(name,function);
            return new ResponseEntity<>(data,HttpStatus.CREATED);
        }catch (CinemaPersistenceException e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.FORBIDDEN);
        }
    }
    @DeleteMapping(value="/cinemas/{name}/{date}/{moviename}")
    public ResponseEntity<?> deleteFunctionByCinemaDateAndMovieName(@PathVariable String name,@PathVariable String date,@PathVariable String moviename){
        Map<String,Object> response = new HashMap<>();
        try{
            cinemaServices.deleteFunctionByCinemaDateAndMovieName(name,date,moviename);
            response.put("Mensaje","Eliminado correctamente");
            return new ResponseEntity<>(response,HttpStatus.OK);
        }catch (CinemaPersistenceException e){
            response.put("Mensaje",e.getMessage());
            return new ResponseEntity<>(response,HttpStatus.NOT_FOUND);
        }
    }



}
