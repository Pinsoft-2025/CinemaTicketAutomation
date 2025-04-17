package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.MailBody;
import com.example.CinemaTicketAutomation.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/email")
public class EmailController {
    private final EmailService emailService;

    @Deprecated
    @PostMapping("/password-reset")
    public ResponseEntity<String> sendPasswordResetEmail(@RequestParam String email) {
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
                .header("Location", "/api/password-reset/request?email=" + email)
                .body("Bu endpoint kullanımdan kaldırılmıştır. Lütfen '/api/password-reset/request' endpoint'ini kullanın.");
    }

    @PostMapping("/cancellation-request")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> sendCancellationRequest(
            @RequestParam String userEmail,
            @RequestParam String ticketInfo) {
        try {
            emailService.sendTicketCancellationRequestEmailToAdmin(userEmail, ticketInfo);
            return ResponseEntity.ok("İptal talebi admin'e gönderildi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("E-posta gönderilemedi: " + e.getMessage());
        }
    }

    @PostMapping("/cancellation-response")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> sendCancellationResponse(
            @RequestParam String userEmail,
            @RequestParam boolean approved) {
        try {
            emailService.sendAdminResponseToUser(userEmail, approved);
            return ResponseEntity.ok("Yanıt kullanıcıya gönderildi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("E-posta gönderilemedi: " + e.getMessage());
        }
    }

    // Bu metot genellikle TicketService veya ReservationService içerisinden çağrılır
    // Burada sadece test amaçlı bir endpoint olarak ekliyoruz
    @PostMapping("/reservation-confirmation")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> sendReservationConfirmation(
            @RequestParam String userEmail,
            @RequestParam String reservationDetails) {
        try {
            emailService.sendReservationConfirmation(userEmail, reservationDetails);
            return ResponseEntity.ok("Rezervasyon onayı gönderildi");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("E-posta gönderilemedi: " + e.getMessage());
        }
    }
}
