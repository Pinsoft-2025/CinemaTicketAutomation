package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.request.SeanceCreateDto;
import com.example.CinemaTicketAutomation.dto.request.SeanceSearchDto;
import com.example.CinemaTicketAutomation.dto.request.TimeSlotCheckDto;
import com.example.CinemaTicketAutomation.dto.response.SeanceDto;
import com.example.CinemaTicketAutomation.entity.Seance;
import com.example.CinemaTicketAutomation.entity.enums.MovieFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface SeanceService {
    SeanceDto createSeance(SeanceCreateDto createDto);
    SeanceDto updateSeance(Long seanceId, SeanceCreateDto updateDto);
    void deleteSeance(long seanceId);
    SeanceDto getSeance(long seanceId);

    List<SeanceDto> getAllSeance();
    List<SeanceDto> getSeanceByMovie(long movieId);
    List<SeanceDto> getSeanceByHall(long hallId);
    List<SeanceDto> getSeanceByDate(LocalDate date);
    List<SeanceDto> findAvailableSeances(SeanceSearchDto searchCriteria);
    boolean isHallAvailable(TimeSlotCheckDto timeSlot);
}
