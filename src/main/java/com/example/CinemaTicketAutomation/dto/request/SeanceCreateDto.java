package com.example.CinemaTicketAutomation.dto.request;

import com.example.CinemaTicketAutomation.entity.enums.MovieFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalTime;

public record SeanceCreateDto(
        @Schema(description = "Seans başlangıç saati", example = "14:30", required = true)
        @JsonFormat(pattern = "HH:mm")
        LocalTime startTime,

        @Schema(description = "Ara süresi", example = "00:15", required = true)
        @JsonFormat(pattern = "HH:mm")
        LocalTime breakTime,

        @Schema(description = "Seans tarihi", example = "2025-12-18", required = true)
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,

        @Schema(description = "Dublaj dili", example = "Türkçe")
        String dubLanguage,

        @Schema(description = "Altyazı var mı?", example = "true")
        boolean hasSubtitle,

        @Schema(description = "Altyazı dili", example = "İngilizce")
        String subLanguage,

        @Schema(description = "Film formatı", example = "TWO_D", required = true)
        MovieFormat format,

        @Schema(description = "Film ID", example = "1", required = true)
        Long movieId,

        @Schema(description = "Salon ID", example = "1", required = true)
        Long hallId
) {}
