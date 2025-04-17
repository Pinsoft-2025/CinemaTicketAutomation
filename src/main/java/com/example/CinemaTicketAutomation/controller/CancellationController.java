package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.CancellationRequestDto;
import com.example.CinemaTicketAutomation.dto.request.CancellationResponseDto;
import com.example.CinemaTicketAutomation.dto.response.TicketCancellationDto;
import com.example.CinemaTicketAutomation.entity.enums.CancellationStatus;
import com.example.CinemaTicketAutomation.service.CancellationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cancellations")
public class CancellationController {
    
    private final CancellationService cancellationService;
    
    @PostMapping("/request")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> requestCancellation(@RequestBody CancellationRequestDto request) {
        Long cancellationId = cancellationService.requestTicketCancellation(
            request.ticketId(), request.reason());
        return ResponseEntity.ok(cancellationId);
    }
    
    @PostMapping("/process/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> processCancellation(
            @PathVariable("id") Long cancellationId,
            @RequestBody CancellationResponseDto response) {
        cancellationService.processAdminResponse(
            cancellationId, response.approved(), response.adminNote());
        return ResponseEntity.ok().build();
    }
    
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketCancellationDto>> getPendingRequests() {
        return ResponseEntity.ok(cancellationService.getPendingCancellationRequests());
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<TicketCancellationDto> getCancellationById(@PathVariable Long id) {
        return ResponseEntity.ok(cancellationService.getCancellationById(id));
    }
    
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketCancellationDto>> getCancellationsByStatus(
            @PathVariable CancellationStatus status) {
        return ResponseEntity.ok(cancellationService.getCancellationsByStatus(status));
    }
} 