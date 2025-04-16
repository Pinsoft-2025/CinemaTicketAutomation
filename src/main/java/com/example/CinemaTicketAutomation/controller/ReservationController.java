package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.ReservationCreateDto;
import com.example.CinemaTicketAutomation.dto.response.ReservationDto;
import com.example.CinemaTicketAutomation.dto.response.ReservationWithTicketsDto;
import com.example.CinemaTicketAutomation.entity.enums.PaymentStatus;
import com.example.CinemaTicketAutomation.service.ReservationDetailService;
import com.example.CinemaTicketAutomation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@Tag(name = "Rezervasyon İşlemleri", description = "Rezervasyon oluşturma, sorgulama ve yönetimi için API")
public class ReservationController {
    
    private final ReservationService reservationService;
    private final ReservationDetailService reservationDetailService;
    
    @PostMapping("/user/create")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Rezervasyon oluştur", description = "Yeni bir rezervasyon oluşturur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rezervasyon başarıyla oluşturuldu"),
        @ApiResponse(responseCode = "400", description = "Geçersiz rezervasyon bilgileri", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content)
    })
    public ResponseEntity<ReservationDto> createReservation(
            @Parameter(description = "Rezervasyon bilgileri", required = true)
            @RequestBody ReservationCreateDto createDto) {
        return ResponseEntity.ok(reservationService.createInitialReservation(createDto));
    }
    
    @GetMapping("/admin/find-by-id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Rezervasyon bilgilerini getir", description = "ID'ye göre rezervasyon bilgilerini getirir (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rezervasyon başarıyla alındı"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Rezervasyon bulunamadı", content = @Content)
    })
    public ResponseEntity<ReservationDto> getReservation(
            @Parameter(description = "Rezervasyon ID", required = true)
            @PathVariable long id) {
        return ResponseEntity.ok(reservationService.getReservationById(id));
    }
    
    @GetMapping("/admin/find-by-user/{userId}")
    @Operation(summary = "Kullanıcı rezervasyonlarını getir", description = "Belirli bir kullanıcının tüm rezervasyonlarını listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rezervasyonlar başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı", content = @Content)
    })
    public ResponseEntity<List<ReservationDto>> getReservationsByUser(
            @Parameter(description = "Kullanıcı ID", required = true)
            @PathVariable long userId) {
        return ResponseEntity.ok(reservationService.getReservationsByUserId(userId));
    }
    
    @PutMapping("/admin/{reservationId}/payment-status")
    @Operation(summary = "Ödeme durumunu güncelle", description = "Bir rezervasyonun ödeme durumunu günceller")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Ödeme durumu başarıyla güncellendi"),
        @ApiResponse(responseCode = "404", description = "Rezervasyon bulunamadı", content = @Content)
    })
    public ResponseEntity<ReservationDto> updatePaymentStatus(
            @Parameter(description = "Rezervasyon ID", required = true)
            @PathVariable long reservationId,
            @Parameter(description = "Ödeme durumu", required = true, schema = @Schema(implementation = PaymentStatus.class))
            @RequestParam PaymentStatus status) {
        return ResponseEntity.ok(reservationService.updatePaymentStatus(reservationId, status));
    }

//  Cancel ile birlikte gelecek
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteReservation(@PathVariable long id) {
//        reservationService.deleteReservation(id);
//        return ResponseEntity.noContent().build();
//    }
    
    // Biletleri dahil eden yeni endpointler
    
    @GetMapping("/{id}/with-tickets")
    @Operation(summary = "Biletleri ile rezervasyon getir", description = "Bir rezervasyonu ve ona ait tüm biletleri getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rezervasyon ve biletler başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Rezervasyon bulunamadı", content = @Content)
    })
    public ResponseEntity<ReservationWithTicketsDto> getReservationWithTickets(
            @Parameter(description = "Rezervasyon ID", required = true)
            @PathVariable long id) {
        return ResponseEntity.ok(reservationDetailService.getReservationWithTickets(id));
    }
} 