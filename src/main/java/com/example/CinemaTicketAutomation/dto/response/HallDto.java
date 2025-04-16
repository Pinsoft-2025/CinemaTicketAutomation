package com.example.CinemaTicketAutomation.dto.response;

public record HallDto(
    Long id,
    String hallNo,
    String maxRow,
    int maxCol
) {} 