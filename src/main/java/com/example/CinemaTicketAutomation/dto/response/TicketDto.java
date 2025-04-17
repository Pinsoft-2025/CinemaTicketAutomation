package com.example.CinemaTicketAutomation.dto.response;

import com.example.CinemaTicketAutomation.entity.enums.TicketType;
import com.example.CinemaTicketAutomation.entity.enums.TicketStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
public record TicketDto(
    Long id,
    TicketType type,
    BigDecimal price,
    String barcode,
    LocalDateTime issuedAt,
    Long reservationId,
    SeanceSeatDto seanceSeat,
    TicketStatus status,
    
    // Ekstra bilgiler - front-end için faydalı
    String movieTitle,
    String hallNo,
    String seatNo,
    LocalDateTime seanceDateTime
) {
    public boolean isCancelled() {
        return status == TicketStatus.CANCELLED;
    }
} 