package com.example.CinemaTicketAutomation.dto.request;

public record RegisterRequest(
    String username,
    String password,
    String email,
    String firstName,
    String lastName
) {} 