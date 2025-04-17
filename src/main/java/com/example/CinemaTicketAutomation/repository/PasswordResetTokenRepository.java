package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    
    /**
     * Token değerine göre token kaydını bulur
     */
    Optional<PasswordResetToken> findByToken(String token);
    
    /**
     * Kullanıcı ID'sine göre token kayıtlarını siler
     */
    void deleteByUserId(Long userId);
    
    /**
     * Süresi geçmiş token kayıtlarını siler
     */
    @Modifying
    @Query("DELETE FROM PasswordResetToken t WHERE t.expiryDate < :date")
    void deleteAllExpiredSince(@Param("date") LocalDateTime date);
} 