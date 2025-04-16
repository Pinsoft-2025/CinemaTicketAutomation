package com.example.CinemaTicketAutomation.dto.request;

import com.example.CinemaTicketAutomation.entity.enums.PaymentStatus;

public record PaymentUpdateDto(
    PaymentStatus paymentStatus
) {} 