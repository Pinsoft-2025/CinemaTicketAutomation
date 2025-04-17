package com.example.CinemaTicketAutomation.service;

/**
 * Şifre sıfırlama işlemlerini yöneten servis
 */
public interface ResetPasswordService {
    
    /**
     * Kullanıcının e-posta adresine şifre sıfırlama token'ı gönderir
     * @param email Kullanıcının e-posta adresi
     * @return İşlemin başarılı olup olmadığı
     */
    boolean initiatePasswordReset(String email);
    
    /**
     * Token'ı doğrular ve geçerliyse kullanıcıya yeni şifre belirlemesine izin verir
     * @param token Şifre sıfırlama token'ı
     * @return Token geçerli ise true, değilse false
     */
    boolean validateToken(String token);
    
    /**
     * Kullanıcının şifresini değiştirir
     * @param token Şifre sıfırlama token'ı
     * @param newPassword Yeni şifre
     * @return İşlemin başarılı olup olmadığı
     */
    boolean resetPassword(String token, String newPassword);
}
