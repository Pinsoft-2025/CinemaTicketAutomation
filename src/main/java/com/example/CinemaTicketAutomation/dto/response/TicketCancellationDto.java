package com.example.CinemaTicketAutomation.dto.response;

import com.example.CinemaTicketAutomation.entity.enums.CancellationStatus;

import java.time.LocalDateTime;

public record TicketCancellationDto(
    Long id,
    Long ticketId,
    String reason,
    CancellationStatus status,
    LocalDateTime requestDate,
    LocalDateTime processDate,
    String adminNote,
    TicketDto ticket
) {} 