package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.enums.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findByGenresContaining(String genre);
    List<Movie> findByDirector(String director);
    List<Movie> findByCountry(Country country);
    List<Movie> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String keyword, String keyword1);
}
