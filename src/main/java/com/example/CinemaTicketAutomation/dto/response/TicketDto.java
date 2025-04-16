package com.example.CinemaTicketAutomation.dto.response;

import com.example.CinemaTicketAutomation.entity.enums.TicketType;
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
    
    // Ekstra bilgiler - front-end için faydalı
    String movieTitle,
    String hallNo,
    String seatNo,
    LocalDateTime seanceDateTime
) {} 