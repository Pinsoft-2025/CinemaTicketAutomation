package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.response.TicketCancellationDto;
import com.example.CinemaTicketAutomation.entity.enums.CancellationStatus;

import java.util.List;

public interface CancellationService {
    Long requestTicketCancellation(Long ticketId, String reason);
    void processAdminResponse(Long cancellationId, boolean approved, String adminNote);
    List<TicketCancellationDto> getPendingCancellationRequests();
    TicketCancellationDto getCancellationById(Long cancellationId);
    List<TicketCancellationDto> getCancellationsByStatus(CancellationStatus status);
} 