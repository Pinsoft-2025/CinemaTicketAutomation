package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.AuthRequest;
import com.example.CinemaTicketAutomation.dto.request.RegisterRequest;
import com.example.CinemaTicketAutomation.dto.response.AuthResponse;
import com.example.CinemaTicketAutomation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    
    // Kullanıcı giriş yapabilir
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    
    // Kullanıcı kayıt olabilir
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
} 