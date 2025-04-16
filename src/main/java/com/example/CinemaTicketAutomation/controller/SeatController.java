package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.response.SeatDto;
import com.example.CinemaTicketAutomation.entity.Seat;
import com.example.CinemaTicketAutomation.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    // Admin yetkili endpoints
    
    @PostMapping("/admin/add-to-hall/{hallId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatDto> addSeatToHall(@PathVariable Long hallId) {
        Seat seat = seatService.addNextSeatToHall(hallId);
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.getSeatDto(seat.getId()));
    }
    
    @PutMapping("/admin/update/{seatId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeatDto> updateSeat(@PathVariable Long seatId, @RequestBody Seat seat) {
        seat.setId(seatId);
        Seat updatedSeat = seatService.updateSeat(seat);
        return ResponseEntity.ok(seatService.getSeatDto(updatedSeat.getId()));
    }
    
    @DeleteMapping("/admin/delete/{seatId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSeat(@PathVariable Long seatId) {
        seatService.deleteSeat(seatId);
        return ResponseEntity.noContent().build();
    }
    
    // Hem admin hem kullanıcı için endpoints
    
    @GetMapping("/find-by-id/{seatId}")
    public ResponseEntity<SeatDto> getSeatById(@PathVariable Long seatId) {
        return ResponseEntity.ok(seatService.getSeatDto(seatId));
    }
    
    @GetMapping("/find-by-hall/{hallId}")
    public ResponseEntity<List<SeatDto>> getAllSeatsByHallId(@PathVariable Long hallId) {
        return ResponseEntity.ok(seatService.getAllSeatDtosByHallId(hallId));
    }
}
