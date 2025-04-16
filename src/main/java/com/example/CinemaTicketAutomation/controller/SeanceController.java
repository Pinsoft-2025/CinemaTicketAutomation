package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.SeanceCreateDto;
import com.example.CinemaTicketAutomation.dto.request.SeanceSearchDto;
import com.example.CinemaTicketAutomation.dto.response.SeanceDto;
import com.example.CinemaTicketAutomation.dto.response.SeanceSeatDto;
import com.example.CinemaTicketAutomation.service.SeanceService;
import com.example.CinemaTicketAutomation.service.SeanceSeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/seances")
@RequiredArgsConstructor
public class SeanceController {

    private final SeanceService seanceService;

    // Admin seans ekleyebilir
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeanceDto> createSeance(@RequestBody SeanceCreateDto seanceCreateDto) {
        return ResponseEntity.ok(seanceService.createSeance(seanceCreateDto));
    }

    // Admin seans güncelleyebilir
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SeanceDto> updateSeance(@PathVariable Long id, @RequestBody SeanceCreateDto seanceDto) {
        return ResponseEntity.ok(seanceService.updateSeance(id, seanceDto));
    }

    // Admin seans silebilir
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSeance(@PathVariable Long id) {
        seanceService.deleteSeance(id);
        return ResponseEntity.noContent().build();
    }

    // Hem kullanıcı hem admin için seans görüntüleme işlemleri

    // Tüm seansları listeleme
    @GetMapping
    public ResponseEntity<List<SeanceDto>> getAllSeances() {
        return ResponseEntity.ok(seanceService.getAllSeance());
    }

    // Seans detayı görüntüleme
    @GetMapping("/{id}")
    public ResponseEntity<SeanceDto> getSeanceById(@PathVariable Long id) {
        return ResponseEntity.ok(seanceService.getSeance(id));
    }

    // Belirli bir filme ait seansları listeleme
    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<SeanceDto>> getSeancesByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(seanceService.getSeanceByMovie(movieId));
    }

    // Belirli bir tarihe ait seansları listeleme
    @GetMapping("/date")
    public ResponseEntity<List<SeanceDto>> getSeancesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(seanceService.getSeanceByDate(date));
    }

    // Belirli bir salona ait seansları listeleme
    @GetMapping("/hall/{hallId}")
    public ResponseEntity<List<SeanceDto>> getSeancesByHall(@PathVariable Long hallId) {
        return ResponseEntity.ok(seanceService.getSeanceByHall(hallId));
    }

    @PostMapping("/search")
    public ResponseEntity<List<SeanceDto>> searchSeances(@RequestBody SeanceSearchDto searchDto) {
        return ResponseEntity.ok(seanceService.findAvailableSeances(searchDto));
    }

}