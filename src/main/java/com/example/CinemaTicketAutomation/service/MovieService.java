package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.request.MovieCreateDto;
import com.example.CinemaTicketAutomation.dto.response.MovieDto;
import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.enums.Country;
import com.example.CinemaTicketAutomation.entity.enums.Genre;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {
    // CRUD operations
    MovieDto createMovie(MovieCreateDto movieDto);
    MovieDto updateMovie(Long movieId, MovieDto movieDto);
    void deleteMovie(Long movieId);
    MovieDto getMovieById(Long movieId);
    Movie getMovieEntity(Long movieId);

    // Search operations
    List<MovieDto> getAllMovies();
    List<MovieDto> getMoviesByGenre(Genre genre);
    List<MovieDto> getUpcomingMovies();
    List<MovieDto> getMoviesByReleaseDate(LocalDate releaseDate);
    List<MovieDto> searchMoviesByTitle(String keyword);
    List<MovieDto> getMoviesByDirector(String director);
    List<MovieDto> getMoviesByCountry(Country country);
    List<MovieDto> getMoviesByDateRange(LocalDate startDate, LocalDate endDate);
    long getMovieCount();
}
