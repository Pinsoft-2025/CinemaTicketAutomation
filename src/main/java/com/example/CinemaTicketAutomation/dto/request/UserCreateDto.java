package com.example.CinemaTicketAutomation.dto.request;

public record UserCreateDto(
        String username,
        String password,
        String mail,
        String phoneNumber,
        String firstname,
        String lastname,
        int age
) {
}
