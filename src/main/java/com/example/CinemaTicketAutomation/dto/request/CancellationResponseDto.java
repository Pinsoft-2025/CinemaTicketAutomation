package com.example.CinemaTicketAutomation.dto.request;

import lombok.Builder;

@Builder
public record CancellationResponseDto(
    boolean approved,
    String adminNote
) {} 