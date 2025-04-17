package com.example.CinemaTicketAutomation.dto.request;

import lombok.Builder;

/**
 * Token doğrulama isteği için DTO
 */
@Builder
public record ValidateTokenRequestDto(
        String token
) {} 