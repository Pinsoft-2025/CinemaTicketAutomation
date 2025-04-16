package com.example.CinemaTicketAutomation.dto.request;

import com.example.CinemaTicketAutomation.entity.enums.PaymentMethod;
import com.example.CinemaTicketAutomation.entity.enums.TicketType;

import java.util.List;

public record TicketCreateDto(
    List<Long> seanceSeatIds,
    List<TicketType> ticketTypes,
    Long userId,
    PaymentMethod paymentMethod
) {} 