package edu.eci.arsw.cinema.filter.impl;

import edu.eci.arsw.cinema.filter.CinemaFilterException;
import edu.eci.arsw.cinema.filter.CinemaFilterI;
import edu.eci.arsw.cinema.model.Cinema;
import edu.eci.arsw.cinema.model.CinemaFunction;
import edu.eci.arsw.cinema.model.Movie;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;


@Component
@Qualifier("FilterByG")
public class CinemaFilterByGender implements CinemaFilterI {
    @Override
    public List<Movie> filerMovie(Cinema cinema, String date, String filter) throws CinemaFilterException {
        if (isNumeric(filter)){
            throw new CinemaFilterException(CinemaFilterException.ms1);
        }
        String gender = filter;
        List<Movie> moviesByGender = new LinkedList<Movie>();
        List<CinemaFunction> functions = cinema.getFunctions();
        for (CinemaFunction cf: functions) {
            if(cf.getMovie().getGenre().equals(gender) && cf.getDate().equals(date)){
                moviesByGender.add(cf.getMovie());
            }
        }
        return moviesByGender;
    }
    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

}


