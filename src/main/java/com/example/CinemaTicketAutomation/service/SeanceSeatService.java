package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.response.SeanceSeatDto;
import com.example.CinemaTicketAutomation.entity.SeanceSeat;
import com.example.CinemaTicketAutomation.entity.enums.SeatStatus;

import java.util.List;

public interface SeanceSeatService {
    List<SeanceSeatDto> initializeSeanceSeats(long seanceId);
    List<SeanceSeatDto> getSeanceSeatsBySeanceId(long seanceId);
    SeanceSeatDto getSeanceSeat(long seanceSeatId);
    SeanceSeatDto updateSeanceSeatStatus(long seanceSeatId, SeatStatus status);
    List<SeanceSeatDto> getFreeSeatsForSeance(long seanceId);
    List<SeanceSeatDto> reserveSeats(List<Long> seanceSeatIds);
    SeanceSeat findAndValidateSeanceSeat(Long seanceSeatId);
    void updateSeatStatus(SeanceSeat seanceSeat, SeatStatus status);
}
