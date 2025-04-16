package com.example.CinemaTicketAutomation.dto.response;

import com.example.CinemaTicketAutomation.entity.enums.SeatStatus;

public record SeanceSeatDto(
    Long id,
    Long seanceId,
    Long seatId,
    SeatStatus status,
    boolean taken
) {} 