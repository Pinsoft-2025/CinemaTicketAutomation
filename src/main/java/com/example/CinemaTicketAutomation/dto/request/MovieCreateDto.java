package com.example.CinemaTicketAutomation.dto.request;

import com.example.CinemaTicketAutomation.entity.enums.Country;
import com.example.CinemaTicketAutomation.entity.enums.Genre;
import com.example.CinemaTicketAutomation.entity.enums.Warning;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public record MovieCreateDto(
        String title,
        String description,
        String posterBase64,
        MultipartFile posterFile,
        String trailerURI,
        int durationMin,
        List<Genre> genres,
        String director,
        Country country,
        LocalDate releaseDate,
        List<Warning> warnings
) {} 