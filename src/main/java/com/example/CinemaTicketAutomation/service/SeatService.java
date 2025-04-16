package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.response.SeatDto;
import com.example.CinemaTicketAutomation.entity.Seat;

import java.util.List;

public interface SeatService {
    Seat addNextSeatToHall(long hallId);
    Seat updateSeat(Seat seat);
    void deleteSeat(long seatId);
    Seat getSeat(long seatId);
    SeatDto getSeatDto(long seatId);

    List<Seat> getAllSeatsByHallId(long hallId);
    List<SeatDto> getAllSeatDtosByHallId(long hallId);
}
