package com.example.CinemaTicketAutomation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * Şifre sıfırlama isteği için DTO
 */
@Builder
public record PasswordResetRequestDto(
        @NotBlank(message = "Token boş olamaz")
        String token,
        
        @NotBlank(message = "Şifre boş olamaz")
        @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$",
            message = "Şifre en az 8 karakter uzunluğunda olmalı ve en az bir büyük harf, bir küçük harf, bir rakam ve bir özel karakter içermelidir"
        )
        String newPassword
) {} 