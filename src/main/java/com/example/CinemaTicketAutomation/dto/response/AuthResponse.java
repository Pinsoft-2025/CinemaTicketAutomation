package com.example.CinemaTicketAutomation.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
    String token,
    Long userId,
    String username,
    String role
) {} 