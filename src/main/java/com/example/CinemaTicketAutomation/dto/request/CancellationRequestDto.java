package com.example.CinemaTicketAutomation.dto.request;

import lombok.Builder;

@Builder
public record CancellationRequestDto(
    Long ticketId,
    String reason
) {} 