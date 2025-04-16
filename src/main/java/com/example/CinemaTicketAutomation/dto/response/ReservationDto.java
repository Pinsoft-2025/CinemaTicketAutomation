package com.example.CinemaTicketAutomation.dto.response;

import com.example.CinemaTicketAutomation.entity.enums.PaymentMethod;
import com.example.CinemaTicketAutomation.entity.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {
    private Long id;
    private LocalDateTime reservationTime;
    private BigDecimal totalPrice;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private UserDto user;
    private List<TicketDto> tickets;
} 