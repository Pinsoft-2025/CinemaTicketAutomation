package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.request.TicketCreateDto;
import com.example.CinemaTicketAutomation.dto.response.TicketDto;
import com.example.CinemaTicketAutomation.entity.enums.TicketType;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketService {
    // Temel işlemler - sadece DTO döndüren metodlar
    List<TicketDto> createTickets(TicketCreateDto ticketCreateDto);
    List<TicketDto> getTicketsByReservationId(long reservationId);
    TicketDto getTicketById(long ticketId);
    List<TicketDto> getTicketsByUserId(long userId);
    
    // Admin metodları
    List<TicketDto> getAllTickets();
    TicketDto getTicketBySeanceSeatId(Long seanceSeatId);
    List<TicketDto> getTicketsByIssuedAtBetween(LocalDateTime start, LocalDateTime end);
    List<TicketDto> getTicketsByType(TicketType type);

    // User metodları
    TicketDto getTicketByBarcode(String barcode);
}
