package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
}
