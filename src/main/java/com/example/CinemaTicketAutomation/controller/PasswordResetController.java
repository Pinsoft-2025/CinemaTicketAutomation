package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.PasswordResetRequestDto;
import com.example.CinemaTicketAutomation.dto.request.ValidateTokenRequestDto;
import com.example.CinemaTicketAutomation.service.ResetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/password-reset")
@RequiredArgsConstructor
public class PasswordResetController {

    private final ResetPasswordService resetPasswordService;

    @PostMapping("/request")
    public ResponseEntity<String> requestPasswordReset(@RequestParam String email) {
        log.info("Şifre sıfırlama isteği alındı: {}", email);
        resetPasswordService.initiatePasswordReset(email);
        // Güvenlik nedeniyle her zaman başarılı yanıt dön (kullanıcının var olup olmadığını belli etme)
        return ResponseEntity.ok("Şifre sıfırlama e-postası gönderildi.");
    }

    @PostMapping("/validate-token")
    public ResponseEntity<?> validateToken(@Valid @RequestBody ValidateTokenRequestDto request) {
        log.info("Token doğrulama isteği alındı");
        boolean isValid = resetPasswordService.validateToken(request.token());
        
        if (isValid) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.badRequest().body("Geçersiz veya süresi dolmuş token.");
        }
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequestDto request) {
        log.info("Şifre değiştirme isteği alındı");
        boolean success = resetPasswordService.resetPassword(request.token(), request.newPassword());
        
        if (success) {
            return ResponseEntity.ok("Şifre başarıyla güncellendi.");
        } else {
            return ResponseEntity.badRequest().body("Şifre güncellenemedi. Geçersiz veya süresi dolmuş token.");
        }
    }
} 