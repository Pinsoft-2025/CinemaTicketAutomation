package com.example.CinemaTicketAutomation.dto.request;

import com.example.CinemaTicketAutomation.entity.enums.MovieFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record SeanceCreateDto(
    LocalTime startTime,
    LocalTime breakTime,
    LocalDate date,
    String dubLanguage,
    boolean hasSubtitle,
    String subLanguage,
    MovieFormat format,
    Long movieId,
    Long hallId
) {} 