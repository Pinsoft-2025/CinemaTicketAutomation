package com.example.CinemaTicketAutomation.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlotCheckDto(
    Long hallId,
    LocalDate date,
    LocalTime startTime,
    LocalTime endTime
) {} 