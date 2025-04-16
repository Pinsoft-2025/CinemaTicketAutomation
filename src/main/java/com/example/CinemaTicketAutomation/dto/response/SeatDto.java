package com.example.CinemaTicketAutomation.dto.response;

import lombok.Builder;


@Builder
public record SeatDto (
        String seatNo,
        String row,
        int column,
        Long hallId
){}