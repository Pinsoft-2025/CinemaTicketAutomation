package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.entity.PasswordResetToken;
import com.example.CinemaTicketAutomation.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * SADECE DEV veya TEST ortamında aktif olan test endpoint'leri
 * Üretim ortamında bu controller aktif olmayacaktır
 */
@Profile({"dev", "test"})
@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestController {

    private final PasswordResetTokenRepository tokenRepository;

    /**
     * Verilen token'ın bilgilerini döndürür - SADECE TEST ORTAMINDA ÇALIŞIR
     */
    @GetMapping("/token-info")
    public ResponseEntity<?> getTokenInfo(@RequestParam String token) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        
        if (tokenOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        PasswordResetToken resetToken = tokenOptional.get();
        
        Map<String, Object> tokenInfo = new HashMap<>();
        tokenInfo.put("id", resetToken.getId());
        tokenInfo.put("token", resetToken.getToken());
        tokenInfo.put("userId", resetToken.getUser().getId());
        tokenInfo.put("userEmail", resetToken.getUser().getMail());
        tokenInfo.put("expiryDate", resetToken.getExpiryDate());
        tokenInfo.put("isExpired", resetToken.isExpired());
        
        return ResponseEntity.ok(tokenInfo);
    }
} 