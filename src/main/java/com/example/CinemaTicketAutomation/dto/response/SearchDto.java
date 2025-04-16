package com.example.CinemaTicketAutomation.dto.response;

import com.example.CinemaTicketAutomation.entity.enums.Genre;

import java.time.LocalDate;

public record SearchDto(
    String query,
    Genre genre,
    LocalDate startDate,
    LocalDate endDate,
    Integer page,
    Integer size,
    String sortBy,
    String sortDirection
) {} 