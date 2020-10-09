package edu.eci.arsw.cinema.filter;

public class CinemaFilterException extends Exception {

    public static final String ms1 = "El genero no puede ser NUMERICO";
    public static final String ms2 = "La disponibilidad debe ser NUMERICA";

    public CinemaFilterException(String message) {
        super(message);
    }

}
