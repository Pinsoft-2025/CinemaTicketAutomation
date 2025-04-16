package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Seance;
import com.example.CinemaTicketAutomation.entity.enums.MovieFormat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface SeanceRepository extends JpaRepository<Seance, Long> {
    List<Seance> findByMovieId(long movieId);
    List<Seance> findByHallId(long hallId);
    List<Seance> findByDate(LocalDate date);

    @Query("SELECT s FROM Seance s WHERE s.date = :date AND s.movie.id = :movieId AND s.format = :format")
    List<Seance> findByDateAndMovieIdAndFormat(@Param("date") LocalDate date,
                                              @Param("movieId") long movieId,
                                              @Param("format") MovieFormat format);


    @Query("SELECT COUNT(s) > 0 FROM Seance s WHERE s.hall.id = :hallId AND s.date = :date " +
            "AND ((s.startTime <= :endTime AND s.endTime >= :startTime))")
    boolean existsOverlappingSession(@Param("hallId") long hallId,
                                     @Param("date") LocalDate date,
                                     @Param("startTime") LocalTime startTime,
                                     @Param("endTime") LocalTime endTime);}
