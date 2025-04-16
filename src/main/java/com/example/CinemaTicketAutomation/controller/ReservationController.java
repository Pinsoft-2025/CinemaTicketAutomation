package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.ReservationCreateDto;
import com.example.CinemaTicketAutomation.dto.response.ReservationDto;
import com.example.CinemaTicketAutomation.dto.response.ReservationWithTicketsDto;
import com.example.CinemaTicketAutomation.entity.enums.PaymentStatus;
import com.example.CinemaTicketAutomation.service.ReservationDetailService;
import com.example.CinemaTicketAutomation.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    
    private final ReservationService reservationService;
    private final ReservationDetailService reservationDetailService;
    
    @PostMapping("/user/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReservationDto> createReservation(@RequestBody ReservationCreateDto createDto) {
        return ResponseEntity.ok(reservationService.createInitialReservation(createDto));
    }
    
    @GetMapping("/admin/find-by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDto> getReservation(@PathVariable long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }
    
    @GetMapping("/admin/find-by-user/{userId}")
    public ResponseEntity<List<ReservationDto>> getReservationsByUser(@PathVariable long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }
    
    @PutMapping("/admin/{reservationId}/payment-status")
    public ResponseEntity<ReservationDto> updatePaymentStatus(
            @PathVariable long reservationId,
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(reservationService.updatePaymentStatus(reservationId, status));
    }

//  //TODO Cancel ile birlikte gelecek
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
//        reservationService.deleteReservation(id);
//        return ResponseEntity.noContent().build();
//    }
    
    // Biletleri dahil eden yeni endpointler
    
    @GetMapping("/{id}/with-tickets")
    public ResponseEntity<ReservationWithTicketsDto> getReservationWithTickets(@PathVariable long id) {
        return ResponseEntity.ok(reservationDetailService.getReservationWithTickets(id));
    }
} 