package com.example.CinemaTicketAutomation.dto.request;

import lombok.Data;

@Data
public class UserUpdateDto {
    private String username;
    private String mail;
    private String firstname;
    private String lastName;
    private String phoneNumber;
    private int age;
    private String password;
} 