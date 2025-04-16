package com.example.CinemaTicketAutomation.dto.request;

import com.example.CinemaTicketAutomation.entity.enums.MovieFormat;

import java.time.LocalDate;

public record SeanceSearchDto(
    LocalDate date,
    Long movieId,
    MovieFormat format
) {} 