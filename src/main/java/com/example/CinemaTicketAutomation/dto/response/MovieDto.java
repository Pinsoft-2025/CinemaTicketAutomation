package com.example.CinemaTicketAutomation.dto.response;

import com.example.CinemaTicketAutomation.entity.enums.Country;
import com.example.CinemaTicketAutomation.entity.enums.Genre;
import com.example.CinemaTicketAutomation.entity.enums.Warning;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
@Builder
public record MovieDto(
    Long id,
    String title,
    String description,
    String posterBase64,
    String trailerURI,
    int durationMin,
    List<Genre> genres,
    String director,
    Country country,
    LocalDate releaseDate,
    List<Warning> warnings
) {} 