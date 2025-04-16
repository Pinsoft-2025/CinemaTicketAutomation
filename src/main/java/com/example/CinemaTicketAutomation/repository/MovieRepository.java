package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.enums.Country;
import com.example.CinemaTicketAutomation.entity.enums.Genre;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenresContaining(Genre genre);
    List<Movie> findByGenresContaining(Genre genre, Pageable pageable);
    List<Movie> findByDirector(String director);
    List<Movie> findByCountry(Country country);
    List<Movie> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
    List<Movie> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description, Pageable pageable);
    List<Movie> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    List<Movie> findByReleaseDateAfter(LocalDate date);
    List<Movie> findByReleaseDate(LocalDate date);
}
