package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.request.TicketCreateDto;
import com.example.CinemaTicketAutomation.dto.response.TicketDto;

import java.util.List;

public interface TicketService {
    // Temel işlemler - sadece DTO döndüren metodlar
    List<TicketDto> createTickets(TicketCreateDto ticketCreateDto);
    List<TicketDto> getTicketsByReservationId(long reservationId);
    TicketDto getTicketById(long ticketId);
    List<TicketDto> getTicketsByUserId(long userId);
}
