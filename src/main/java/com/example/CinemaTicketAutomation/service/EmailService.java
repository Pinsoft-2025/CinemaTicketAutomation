package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.MailBody;

public interface EmailService {
    void sendEmail(MailBody mailBody);
    void sendPasswordResetEmail(String to, String token);
    void sendTicketCancellationRequestEmailToAdmin(String userEmail, String ticketInfo);
    void sendAdminResponseToUser(String to, boolean approved);
    void sendReservationConfirmation(String to, String reservationDetails);
}
