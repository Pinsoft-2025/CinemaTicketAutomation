package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.TicketCreateDto;
import com.example.CinemaTicketAutomation.dto.response.TicketDto;
import com.example.CinemaTicketAutomation.entity.enums.CancellationStatus;
import com.example.CinemaTicketAutomation.entity.enums.TicketType;
import com.example.CinemaTicketAutomation.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/user/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TicketDto>> createTickets(@RequestBody TicketCreateDto ticketCreateDto) {
        return ResponseEntity.ok(ticketService.createTickets(ticketCreateDto));
    }

    @GetMapping("/user/find-all-own/{userId}")
    @PreAuthorize("hasRole('USER') and @userSecurity.isCurrentUser(#userId)")
    public ResponseEntity<List<TicketDto>> getTicketsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUserId(userId));
    }

    @GetMapping("/user/find-own/{ticketId}")
    @PreAuthorize("hasRole('USER') and @ticketSecurity.canAccessTicket(#ticketId)")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.getTicketById(ticketId));
    }

    @GetMapping("/user/find-own-reservation/{reservationId}")
    @PreAuthorize("hasRole('USER') and @reservationSecurity.canAccessReservation(#reservationId)")
    public ResponseEntity<List<TicketDto>> getTicketsByReservationId(@PathVariable Long reservationId) {
        return ResponseEntity.ok(ticketService.getTicketsByReservationId(reservationId));
    }

    @GetMapping("/user/find-by-barcode/{barcode}")
    @PreAuthorize("hasRole('USER')and @ticketSecurity.canAccessTicket(#ticketId)")
    public ResponseEntity<TicketDto> getTicketsByBarcode(@PathVariable String barcode) {
        return ResponseEntity.ok(ticketService.getTicketByBarcode(barcode));
    }

//    @GetMapping("/user/find-by-barcode-and-status/{barcode}")
//    @PreAuthorize("hasRole('USER')and @ticketSecurity.canAccessTicket(#ticketId)")
//    public ResponseEntity<List<TicketDto>> getTicketsByBarcodeAndStatus(
//            @PathVariable String barcode,
//            @RequestParam CancellationStatus status) {
//        return ResponseEntity.ok(ticketService.getTicketsByBarcodeAndCancellationStatus(barcode, status));
//    }

    @GetMapping("/admin/find-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/admin/find-by-seance-seat/{seanceSeatId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketDto> getTicketsBySeanceSeatId(@PathVariable Long seanceSeatId) {
        return ResponseEntity.ok(ticketService.getTicketBySeanceSeatId(seanceSeatId));
    }

    @GetMapping("/admin/find-by-date-range")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketDto>> getTicketsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(ticketService.getTicketsByIssuedAtBetween(start, end));
    }

    @GetMapping("/admin/find-by-type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketDto>> getTicketsByType(@PathVariable TicketType type) {
        return ResponseEntity.ok(ticketService.getTicketsByType(type));
    }

//    @GetMapping("/admin/find-by-cancellation-status/{status}")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<List<TicketDto>> getTicketsByCancellationStatus(@PathVariable CancellationStatus status) {
//        return ResponseEntity.ok(ticketService.getTicketsByCancellationStatus(status));
//    }
} 