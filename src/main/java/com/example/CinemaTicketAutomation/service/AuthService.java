package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.request.AuthRequest;
import com.example.CinemaTicketAutomation.dto.response.AuthResponse;
import com.example.CinemaTicketAutomation.dto.request.RegisterRequest;
 
public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(RegisterRequest request);
} 