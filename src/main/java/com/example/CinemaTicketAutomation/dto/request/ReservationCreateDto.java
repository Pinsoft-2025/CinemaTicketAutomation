package com.example.CinemaTicketAutomation.dto.request;

import com.example.CinemaTicketAutomation.entity.enums.PaymentMethod;

public record ReservationCreateDto(
    PaymentMethod paymentMethod,
    Long userId
) {} 