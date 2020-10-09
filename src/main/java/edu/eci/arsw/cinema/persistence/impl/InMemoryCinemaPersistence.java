/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arsw.cinema.persistence.impl;

import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import edu.eci.arsw.cinema.persistence.CinemaException;
import edu.eci.arsw.cinema.persistence.CinemaPersistenceException;
import edu.eci.arsw.cinema.persistence.CinemaPersitence;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The type In memory cinema persistence.
 *
 * @author cristian
 */
@Component
@Qualifier("inMemoryCP")
public class InMemoryCinemaPersistence implements CinemaPersitence{
    private final ConcurrentHashMap<String,Cinema> cinemas=new ConcurrentHashMap<>();

    /**
     * Instantiates a new In memory cinema persistence.
     */
    public InMemoryCinemaPersistence() {
        //load stub data
        String functionDate = "2018-12-18 15:30";
        List<CinemaFunction> functions= new ArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("SuperHeroes Movie","Action"),functionDate);
        CinemaFunction funct2 = new CinemaFunction(new Movie("The Night","Horror"),functionDate);
        functions.add(funct1);
        functions.add(funct2);
        Cinema c=new Cinema("cinemaX",functions);
        cinemas.put("cinemaX", c);
        cinemas.put("cinemaD", this.getCinemaD());
        cinemas.put("cinemaF", this.getCinemaF());
    }
    @Override
    public CinemaFunction buyTicket(int row, int col, String cinema, String date, String movieName) throws CinemaException {
        Cinema cinemaTicket = this.cinemas.get(cinema);
        List<CinemaFunction> functionsOfOurCinema=cinemaTicket.getFunctions();
        CinemaFunction cfUpdated = null;
        boolean functionFound=false;
        for (CinemaFunction cf: functionsOfOurCinema) {
            if(cf.getMovie().getName().equals(movieName) && cf.getDate().contains(date)){
                cf.buyTicket(row,col);
                functionFound=true;
                cfUpdated = cf;
                break;
            }
        }
        if (!functionFound){
            throw new CinemaException("No se encontraron funciones con los parametros indicados.");
        } return cfUpdated;

    }
    @Override
    public List<CinemaFunction> getFunctionsbyCinemaAndDate(String cinema, String date){
        List<CinemaFunction> functions = new LinkedList<>();
        Cinema cinemaTicket = this.cinemas.get(cinema);
        List<CinemaFunction> functionsOfOurCinema=cinemaTicket.getFunctions();
        for (CinemaFunction cf: functionsOfOurCinema) {
            if(cf.getDate().contains(date)){
               functions.add(cf);
            }
        }
        return functions;
    }
    @Override
    public void saveCinema(Cinema c) throws CinemaPersistenceException {
        if (cinemas.containsKey(c.getName())){
            throw new CinemaPersistenceException("The given cinema already exists: "+c.getName());
        }
        else{
            cinemas.put(c.getName(),c);
        }   
    }
    @Override
    public Cinema getCinema(String name) throws CinemaPersistenceException {
        if (cinemas.get(name) == null){
            throw new CinemaPersistenceException("Cinema not Found");
        }
        return cinemas.get(name);
    }
    @Override
    public Set<Cinema> getAllCinemas() throws CinemaPersistenceException {
        if (cinemas.isEmpty()){
            throw  new CinemaPersistenceException("No hay cinemas");
        }
        return new HashSet<>(cinemas.values());
    }
    @Override
    public CinemaFunction getFunctionbyCinemaAndDateAndMovie(String name, String date, String moviename) throws CinemaPersistenceException{
        CinemaFunction function = null;
        Cinema cinemaTicket = this.cinemas.get(name);
        List<CinemaFunction> functionsOfOurCinema=cinemaTicket.getFunctions();
        for (CinemaFunction cf: functionsOfOurCinema) {
            if(cf.getDate().contains(date) && cf.getMovie().getName().equals(moviename)){
                function = cf;
            }
        }
        if (function == null){
            throw new CinemaPersistenceException("Not function found");
        }
        return function;
    }
    @Override
    public CinemaFunction addFunctionToCinema(String name, CinemaFunction function) throws CinemaPersistenceException {
        if (cinemas.get(name) == null){
            throw new CinemaPersistenceException("Cinema not found");
        }else{
            Cinema cinema = cinemas.get(name);
            List<List<Boolean>> seats =new ArrayList<>();
            for (int i=0;i<7;i++){
                List<Boolean> row= new ArrayList<>(Arrays.asList(new Boolean[12]));
                for(int j=0;j<12;j++){
                    row.set(j,true);
                }
                seats.add(row);
            }
            function.setSeats(seats);
            cinema.getFunctions().add(function);
            return function;
        }
    }
    @Override
    public CinemaFunction updateFunctionByCinema(String name, CinemaFunction function) throws CinemaPersistenceException {
        Cinema cinema = this.getCinema(name);
        CinemaFunction updatedFunction = null;
        if (cinema == null) {
            throw new CinemaPersistenceException("Not cinema found.");
        }
        List<CinemaFunction> functions = cinema.getFunctions();
        for (CinemaFunction f : functions) {
            if (f.getMovie().getName().equals(function.getMovie().getName())
                    && f.getDate().split(" ")[0].equals(function.getDate().split(" ")[0])) {
                updatedFunction = f;
                f.setDate(function.getDate());
                f.setMovie(function.getMovie());
            }
        }
        if (updatedFunction == null) {
            cinema.addFunction(function);
        }
        return function;
    }

    @Override
    public void addCinema(Cinema c) {
        cinemas.put(c.getName(),c);
    }
    @Override
    public void deleteFunctionByCinemaDateAndMovieName(String name,String date,String moviename) throws CinemaPersistenceException{
        Cinema cinema = null;
        cinema = getCinema(name);
        List<CinemaFunction> functions = cinema.getFunctions();
        for (CinemaFunction f : functions) {
            if(f.getDate().contains(date) && f.getMovie().getName().equals(moviename)){
                functions.remove(f);
                break;
            }
        }
    }

    private Cinema getCinemaD() {
        String functionDate2 = "2020-12-18 15:30";
        List<CinemaFunction> functions= new ArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("Caranchoa2","Action"),functionDate2);
        CinemaFunction funct2 = new CinemaFunction(new Movie("Caranchoa1","Horror"),functionDate2);
        functions.add(funct1);
        functions.add(funct2);
        return new Cinema("cinemaD",functions);
    }
    private Cinema getCinemaF() {
        String functionDate2 = "2019-12-18 15:30";
        List<CinemaFunction> functions= new ArrayList<>();
        CinemaFunction funct1 = new CinemaFunction(new Movie("La patada del mocho 3","Action"),functionDate2);
        CinemaFunction funct2 = new CinemaFunction(new Movie("La patada del mocho 2","Horror"),functionDate2);
        functions.add(funct1);
        functions.add(funct2);
        return new Cinema("cinemaF",functions);
    }
}
