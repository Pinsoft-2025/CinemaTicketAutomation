package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.response.SeanceSeatDto;
import com.example.CinemaTicketAutomation.entity.enums.SeatStatus;
import com.example.CinemaTicketAutomation.service.SeanceSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seance-seats")
@RequiredArgsConstructor
public class SeanceSeatController {

    private final SeanceSeatService seanceSeatService;
    
    // Admin yetkili endpoint'ler
    
    @PostMapping("/admin/initialize/{seanceId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<SeanceSeatDto>> initializeSeanceSeats(@PathVariable Long seanceId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seanceSeatService.initializeSeanceSeats(seanceId));
    }
    
    @PutMapping("/admin/update-status/{seanceSeatId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeanceSeatDto> updateSeanceSeatStatus(
            @PathVariable Long seanceSeatId,
            @RequestParam SeatStatus status) {
        return ResponseEntity.ok(seanceSeatService.updateSeanceSeatStatus(seanceSeatId, status));
    }
    
    // Hem admin hem kullanıcı için endpoint'ler
    
    @GetMapping("/find-by-seance/{seanceId}")
    public ResponseEntity<List<SeanceSeatDto>> getSeanceSeatsBySeanceId(@PathVariable Long seanceId) {
        return ResponseEntity.ok(seanceSeatService.getSeanceSeatsBySeanceId(seanceId));
    }
    
    @GetMapping("/find-by-id/{seanceSeatId}")
    public ResponseEntity<SeanceSeatDto> getSeanceSeat(@PathVariable Long seanceSeatId) {
        return ResponseEntity.ok(seanceSeatService.getSeanceSeat(seanceSeatId));
    }
    
    @GetMapping("/find-free-seats/{seanceId}")
    public ResponseEntity<List<SeanceSeatDto>> getFreeSeatsForSeance(@PathVariable Long seanceId) {
        return ResponseEntity.ok(seanceSeatService.getFreeSeatsForSeance(seanceId));
    }
    
    // Kullanıcı yetkili endpoint'ler
    
    @PostMapping("/user/reserve")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<SeanceSeatDto>> reserveSeats(@RequestBody List<Long> seanceSeatIds) {
        return ResponseEntity.ok(seanceSeatService.reserveSeats(seanceSeatIds));
    }
}
