package com.example.CinemaTicketAutomation.dto.response;

import com.example.CinemaTicketAutomation.entity.enums.PaymentMethod;
import com.example.CinemaTicketAutomation.entity.enums.PaymentStatus;
import com.example.CinemaTicketAutomation.entity.enums.ReservationStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ReservationWithTicketsDto(
    Long id,
    LocalDateTime reservationTime,
    BigDecimal totalPrice,
    PaymentStatus paymentStatus,
    PaymentMethod paymentMethod,
    ReservationStatus status,
    UserDto user,
    List<TicketDto> tickets
) {} 