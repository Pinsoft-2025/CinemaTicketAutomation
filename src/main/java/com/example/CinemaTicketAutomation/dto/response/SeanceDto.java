package com.example.CinemaTicketAutomation.dto.response;

import com.example.CinemaTicketAutomation.entity.enums.MovieFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeanceDto {
    private Long id;
    private LocalTime startTime;
    private LocalTime breakTime;
    private LocalTime endTime;
    private LocalDate date;
    private String dubLanguage;
    private boolean hasSubtitle;
    private String subLanguage;
    private MovieFormat format;
    private MovieDto movie;
    private HallDto hall;
} 