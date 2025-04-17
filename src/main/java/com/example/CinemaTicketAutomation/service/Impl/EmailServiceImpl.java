package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.MailBody;
import com.example.CinemaTicketAutomation.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final Environment environment;
    
    @Value("${spring.mail.username}")
    private String adminEmail;
    
    @Value("${app.frontend.url:https://yourfrontend.com}")
    private String frontendUrl;

    @Override
    public void sendEmail(MailBody mailBody) {
        // Test ortamında e-posta gönderme işlemini logla
        if (isTestProfile()) {
            log.info("TEST ORTAMI E-POSTA GÖNDERME: Alıcı={}, Konu={}, İçerik={}", 
                mailBody.to(), mailBody.subject(), mailBody.body());
            return;
        }
        
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(adminEmail);
        message.setTo(mailBody.to());
        message.setSubject(mailBody.subject());
        message.setText(mailBody.body());

        try {
            mailSender.send(message);
            log.info("E-posta gönderildi: {}", mailBody.to());
        } catch (Exception e) {
            log.error("E-posta gönderme hatası: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public void sendPasswordResetEmail(String to, String token) {
        String resetLink = frontendUrl + "/reset-password?token=" + token;
        MailBody mailBody = MailBody.builder()
                .to(to)
                .subject("Şifre Sıfırlama")
                .body("Şifrenizi sıfırlamak için linke tıklayın: " + resetLink + 
                      "\n\nBu link 30 dakika boyunca geçerlidir. " +
                      "\n\nEğer bu isteği siz yapmadıysanız, lütfen bu e-postayı dikkate almayın.")
                .build();
        sendEmail(mailBody);
    }

    @Override
    public void sendTicketCancellationRequestEmailToAdmin(String userEmail, String ticketInfo) {
        MailBody mailBody = MailBody.builder()
                .to(adminEmail)
                .subject("Bilet İptal Talebi")
                .body("Kullanıcı " + userEmail + " aşağıdaki bileti iptal etmek istiyor:\n" + ticketInfo)
                .build();
        sendEmail(mailBody);
    }

    @Override
    public void sendAdminResponseToUser(String to, boolean approved) {
        String status = approved ? "onaylandı" : "reddedildi";
        MailBody mailBody = MailBody.builder()
                .to(to)
                .subject("İptal Talebi Durumu")
                .body("İptal talebiniz " + status + ".")
                .build();
        sendEmail(mailBody);
    }

    @Override
    public void sendReservationConfirmation(String to, String reservationDetails) {
        MailBody mailBody = MailBody.builder()
                .to(to)
                .subject("Rezervasyon Bilgilendirmesi")
                .body("Rezervasyonunuz başarıyla oluşturuldu:\n" + reservationDetails)
                .build();
        sendEmail(mailBody);
    }
    
    /**
     * Uygulama test profilinde çalışıyor mu kontrolü
     */
    private boolean isTestProfile() {
        for (String profile : environment.getActiveProfiles()) {
            if (profile.equals("test") || profile.equals("dev")) {
                return true;
            }
        }
        return false;
    }
}

