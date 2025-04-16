package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.response.SeatDto;
import com.example.CinemaTicketAutomation.entity.Seat;
import com.example.CinemaTicketAutomation.service.SeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
@RequiredArgsConstructor
@Tag(name = "Koltuk İşlemleri", description = "Salon koltuklarının yönetimi için API")
public class SeatController {

    private final SeatService seatService;

    // Admin yetkili endpoints
    
    @PostMapping("/admin/add-to-hall/{hallId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Salona koltuk ekle", description = "Belirtilen salona yeni bir koltuk ekler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Koltuk başarıyla eklendi"),
        @ApiResponse(responseCode = "400", description = "Salon kapasitesi dolu", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Salon bulunamadı", content = @Content)
    })
    public ResponseEntity<SeatDto> addSeatToHall(
            @Parameter(description = "Salon ID") 
            @PathVariable Long hallId) {
        Seat seat = seatService.addNextSeatToHall(hallId);
        return ResponseEntity.status(HttpStatus.CREATED).body(seatService.getSeatDto(seat.getId()));
    }
    
    @PutMapping("/admin/update/{seatId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Koltuk güncelle", description = "Belirtilen koltuğu günceller")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Koltuk başarıyla güncellendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz koltuk verileri", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Koltuk bulunamadı", content = @Content)
    })
    public ResponseEntity<SeatDto> updateSeat(
            @Parameter(description = "Koltuk ID") 
            @PathVariable Long seatId, 
            @Parameter(description = "Koltuk bilgileri") 
            @RequestBody Seat seat) {
        seat.setId(seatId);
        Seat updatedSeat = seatService.updateSeat(seat);
        return ResponseEntity.ok(seatService.getSeatDto(updatedSeat.getId()));
    }
    
    @DeleteMapping("/admin/delete/{seatId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Koltuk sil", description = "Belirtilen koltuğu siler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Koltuk başarıyla silindi"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Koltuk bulunamadı", content = @Content)
    })
    public ResponseEntity<Void> deleteSeat(
            @Parameter(description = "Koltuk ID") 
            @PathVariable Long seatId) {
        seatService.deleteSeat(seatId);
        return ResponseEntity.noContent().build();
    }
    
    // Hem admin hem kullanıcı için endpoints
    
    @GetMapping("/find-by-id/{seatId}")
    @Operation(summary = "Koltuk detaylarını getir", description = "Belirtilen koltuğun detaylarını getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Koltuk başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Koltuk bulunamadı", content = @Content)
    })
    public ResponseEntity<SeatDto> getSeatById(
            @Parameter(description = "Koltuk ID") 
            @PathVariable Long seatId) {
        return ResponseEntity.ok(seatService.getSeatDto(seatId));
    }
    
    @GetMapping("/find-by-hall/{hallId}")
    @Operation(summary = "Salon koltuklarını getir", description = "Belirtilen salondaki tüm koltukları listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Koltuklar başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Salon bulunamadı", content = @Content)
    })
    public ResponseEntity<List<SeatDto>> getAllSeatsByHallId(
            @Parameter(description = "Salon ID") 
            @PathVariable Long hallId) {
        return ResponseEntity.ok(seatService.getAllSeatDtosByHallId(hallId));
    }
}
