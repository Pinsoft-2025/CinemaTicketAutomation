package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.Seat;
import com.example.CinemaTicketAutomation.entity.enums.Country;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MovieService {
    //crud
    Movie addMovie(Movie movie, MultipartFile posterFile);
    Movie updateMovie(Movie movie, MultipartFile posterFile);
    void deleteMovie(long movieId);
    Movie getMovie(long movieId);

    //search
    List<Movie> getAllMovies();
    List<Movie> getAllMoviesByGenre(String genre);
    List<Movie> getAllMoviesByDirector(String director);
    List<Movie> getAllMoviesByCountry(Country country);
    List<Movie>searchMovie(String keyword);
}
