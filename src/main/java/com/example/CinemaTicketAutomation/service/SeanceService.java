package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.entity.Seance;
import com.example.CinemaTicketAutomation.entity.enums.MovieFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SeanceService {
    Seance createSeance(Seance seance);
    Seance updateSeance(Seance seance);
    void deleteSeance(long seansId);
    Seance getSeance(long seansId);
    List<Seance> getAllSeance();
    List<Seance> getSeanceByMovie(long movieId);
    List<Seance> getSeanceByHall(long hallId);
    List<Seance> getSeanceByDate(LocalDate date);
    List<Seance> getAvailableSeanceByDateAndMovieAndFormat(LocalDate date, long movieId, MovieFormat format);
    boolean isHallAvailableForTimeSlot(long hallId, LocalDate date, LocalTime startTime, LocalTime endTime);
}
