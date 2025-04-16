package com.example.CinemaTicketAutomation.dto.response;

import com.example.CinemaTicketAutomation.entity.enums.Role;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserDto(
    Long id,
    String username,
    String mail,
    String phoneNumber,
    String firstname,
    String lastName,
    int age,
    String role
) {} 