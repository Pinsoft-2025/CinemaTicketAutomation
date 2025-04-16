package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.AuthRequest;
import com.example.CinemaTicketAutomation.dto.request.RegisterRequest;
import com.example.CinemaTicketAutomation.dto.response.AuthResponse;
import com.example.CinemaTicketAutomation.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Kimlik Doğrulama", description = "Kullanıcı girişi ve kayıt işlemleri için API")
public class AuthController {

    private final AuthService authService;
    
    // Kullanıcı giriş yapabilir
    @PostMapping("/login")
    @Operation(summary = "Kullanıcı girişi", description = "Kullanıcı adı ve şifre ile giriş yapılır ve JWT token döndürülür")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Başarılı giriş"),
        @ApiResponse(responseCode = "401", description = "Geçersiz kullanıcı adı veya şifre", content = @Content)
    })
    public ResponseEntity<AuthResponse> login(
            @Parameter(description = "Giriş bilgileri", required = true)
            @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
    
    // Kullanıcı kayıt olabilir
    @PostMapping("/register")
    @Operation(summary = "Kullanıcı kaydı", description = "Yeni kullanıcı oluşturulur ve JWT token döndürülür")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Başarılı kayıt"),
        @ApiResponse(responseCode = "400", description = "Geçersiz kayıt bilgileri", content = @Content),
        @ApiResponse(responseCode = "409", description = "Kullanıcı adı zaten kayıtlı", content = @Content)
    })
    public ResponseEntity<AuthResponse> register(
            @Parameter(description = "Kayıt bilgileri", required = true)
            @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }
} 