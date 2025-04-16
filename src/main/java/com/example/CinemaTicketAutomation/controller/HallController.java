package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.response.HallDto;
import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
public class HallController {

    private final HallService hallService;

    // Admin salon ekleyebilir
    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hall> createHall(@RequestBody Hall hall) {
        return ResponseEntity.ok(hallService.createHall(hall));
    }

    // Admin salon güncelleyebilir
    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Hall> updateHall(@PathVariable Long id, @RequestBody Hall hall) {
        return ResponseEntity.ok(hallService.updateHall(id, hall));
    }

    // Admin salon silebilir
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteHall(@PathVariable Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-hall/{id}")
    public ResponseEntity<Hall> getHall(@PathVariable Long id) {
        return ResponseEntity.ok(hallService.getHall(id));
    }

    @GetMapping("/find-all")
    public ResponseEntity<List<Hall>> getAllHalls() {
        return ResponseEntity.ok(hallService.getAllHalls());
    }

    @GetMapping("/halls/capacity")
    public ResponseEntity<List<Hall>> getHallsWithMinCapacity(@RequestParam int minCapacity) {
        return ResponseEntity.ok(hallService.getHallsWithMinimumCapacity(minCapacity));
    }

}