package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.entity.AppUser;
import com.example.CinemaTicketAutomation.entity.PasswordResetToken;
import com.example.CinemaTicketAutomation.repository.AppUserRepository;
import com.example.CinemaTicketAutomation.repository.PasswordResetTokenRepository;
import com.example.CinemaTicketAutomation.service.EmailService;
import com.example.CinemaTicketAutomation.service.ResetPasswordService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

    private final AppUserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    
    @Value("${app.reset-password.token-expiry-minutes:30}")
    private int tokenExpiryMinutes;
    
    private static final Pattern PASSWORD_PATTERN = 
        Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$");
    
    @Override
    @Transactional
    public boolean initiatePasswordReset(String email) {
         Optional<AppUser> userOptional = userRepository.findByMail(email);
        if (userOptional.isEmpty()) {
            // Güvenlik nedeniyle kullanıcı bulunamasa bile başarılı gibi davranır
            log.info("Şifre sıfırlama: Kullanıcı bulunamadı {}", email);
            return true;
        }
        
        AppUser user = userOptional.get();

        tokenRepository.deleteByUserId(user.getId());

        // Token oluştur
        String token = UUID.randomUUID().toString();

        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(tokenExpiryMinutes));
        tokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(email, token);
        
        log.info("Şifre sıfırlama token'ı oluşturuldu: {}", email);
        return true;
    }
    
    @Override
    public boolean validateToken(String token) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        
        if (tokenOptional.isEmpty()) {
            log.warn("Token doğrulama başarısız: Token bulunamadı");
            return false;
        }
        
        PasswordResetToken resetToken = tokenOptional.get();
        
        boolean isValid = !resetToken.isExpired();
        
        // Süresi geçmiş mi kontrol et
        if (!isValid) {
            log.warn("Token doğrulama başarısız: Token süresi geçmiş - Kullanıcı: {}", 
                     resetToken.getUser().getUsername());
        } else {
            log.info("Token doğrulama başarılı - Kullanıcı: {}", 
                     resetToken.getUser().getUsername());
        }
        
        return isValid;
    }
    
    @Override
    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> tokenOptional = tokenRepository.findByToken(token);
        
        if (tokenOptional.isEmpty()) {
            log.warn("Şifre sıfırlama başarısız: Token bulunamadı");
            return false;
        }
        
        PasswordResetToken resetToken = tokenOptional.get();
        
        // Süresi geçmiş mi kontrol et
        if (resetToken.isExpired()) {
            log.warn("Şifre sıfırlama başarısız: Token süresi geçmiş - Kullanıcı: {}", 
                     resetToken.getUser().getUsername());
            return false;
        }
        
        // Şifre karmaşıklığı kontrolü (DTO validasyonu geçmiş olmalı ama yine de kontrol edelim)
        if (!isStrongPassword(newPassword)) {
            log.warn("Şifre sıfırlama başarısız: Şifre yeterince güçlü değil - Kullanıcı: {}", 
                     resetToken.getUser().getUsername());
            return false;
        }
        
        // Kullanıcıyı bul
        AppUser user = resetToken.getUser();
        
        // Şifreyi güncelle
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        
        // Token'ı sil
        tokenRepository.delete(resetToken);
        
        log.info("Şifre başarıyla sıfırlandı - Kullanıcı: {}", user.getUsername());
        return true;
    }
    
    /**
     * Her 6 saatte bir süreleri geçmiş token'ları temizler
     */
    @Scheduled(fixedRate = 6 * 60 * 60 * 1000) // 6 saatte bir
    public void cleanupExpiredTokens() {
        log.info("Süreleri geçmiş token'lar temizleniyor...");
        tokenRepository.deleteAllExpiredSince(LocalDateTime.now());
        log.info("Token temizleme tamamlandı");
    }
    
    /**
     * Şifre karmaşıklığını kontrol eder. Sifre en az 8 karakter uzunluğunda olmalı,
     * en az bir büyük harf, bir küçük harf, bir rakam ve bir özel karakter içermelidir.
     * 
     * @param password Kontrol edilecek şifre
     * @return Şifre güçlü ise true, değilse false
     */
    private boolean isStrongPassword(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }
}
