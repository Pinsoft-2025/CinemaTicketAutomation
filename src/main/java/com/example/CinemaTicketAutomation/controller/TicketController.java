package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.TicketCreateDto;
import com.example.CinemaTicketAutomation.dto.response.TicketDto;
import com.example.CinemaTicketAutomation.entity.enums.TicketType;
import com.example.CinemaTicketAutomation.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Bilet İşlemleri", description = "Bilet satın alma, sorgulama ve yönetimi için API")
public class TicketController {

    private final TicketService ticketService;

    @PostMapping("/user/create")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Bilet oluştur", description = "Kullanıcı için yeni biletler oluşturur")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Biletler başarıyla oluşturuldu"),
        @ApiResponse(responseCode = "400", description = "Geçersiz bilet verileri", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content)
    })
    public ResponseEntity<List<TicketDto>> createTickets(
            @Parameter(description = "Bilet oluşturma detayları") 
            @RequestBody TicketCreateDto ticketCreateDto) {
        return ResponseEntity.ok(ticketService.createTickets(ticketCreateDto));
    }
    
    @GetMapping("/user/find-all-own/{userId}")
    @PreAuthorize("hasRole('USER') and @userSecurity.isCurrentUser(#userId)")
    @Operation(summary = "Kullanıcı biletlerini getir", description = "Kullanıcının tüm biletlerini listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Biletler başarıyla alındı"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı", content = @Content)
    })
    public ResponseEntity<List<TicketDto>> getTicketsByUserId(
            @Parameter(description = "Kullanıcı ID") 
            @PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getTicketsByUserId(userId));
    }
    
    @GetMapping("/user/find-own/{ticketId}")
    @PreAuthorize("hasRole('USER') and @ticketSecurity.canAccessTicket(#ticketId)")
    @Operation(summary = "Bilet detaylarını getir", description = "Kullanıcının belirli bir biletinin detaylarını getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bilet başarıyla alındı"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Bilet bulunamadı", content = @Content)
    })
    public ResponseEntity<TicketDto> getTicketById(
            @Parameter(description = "Bilet ID") 
            @PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.getTicketById(ticketId));
    }
    
    @GetMapping("/user/find-own-reservation/{reservationId}")
    @PreAuthorize("hasRole('USER') and @reservationSecurity.canAccessReservation(#reservationId)")
    @Operation(summary = "Rezervasyona ait biletleri getir", description = "Kullanıcının belirli bir rezervasyonuna ait biletleri listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Biletler başarıyla alındı"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Rezervasyon bulunamadı", content = @Content)
    })
    public ResponseEntity<List<TicketDto>> getTicketsByReservationId(
            @Parameter(description = "Rezervasyon ID") 
            @PathVariable Long reservationId) {
        return ResponseEntity.ok(ticketService.getTicketsByReservationId(reservationId));
    }
    
    @GetMapping("/user/find-by-barcode/{barcode}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Barkod ile bilet sorgula", description = "Verilen barkod ile bilet bilgisini sorgular")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bilet başarıyla bulundu"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Bilet bulunamadı", content = @Content)
    })
    public ResponseEntity<TicketDto> getTicketsByBarcode(
            @Parameter(description = "Barkod") 
            @PathVariable String barcode) {
        return ResponseEntity.ok(ticketService.getTicketByBarcode(barcode));
    }
    
    @GetMapping("/admin/find-all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tüm biletleri getir", description = "Sistemdeki tüm biletleri listeler (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Biletler başarıyla alındı"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content)
    })
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }
    
    @GetMapping("/admin/find-by-seance-seat/{seanceSeatId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Seans koltuğuna göre bilet getir", description = "Belirli bir seans koltuğuna ait bilet bilgisini getirir (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Bilet başarıyla alındı"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Bilet bulunamadı", content = @Content)
    })
    public ResponseEntity<TicketDto> getTicketsBySeanceSeatId(
            @Parameter(description = "Seans Koltuk ID") 
            @PathVariable Long seanceSeatId) {
        return ResponseEntity.ok(ticketService.getTicketBySeanceSeatId(seanceSeatId));
    }
    
    @GetMapping("/admin/find-by-date-range")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tarih aralığına göre biletleri getir", description = "Belirli bir tarih aralığında oluşturulmuş biletleri listeler (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Biletler başarıyla alındı"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "400", description = "Geçersiz tarih formatı", content = @Content)
    })
    public ResponseEntity<List<TicketDto>> getTicketsByDateRange(
            @Parameter(description = "Başlangıç tarihi (ISO formatında)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @Parameter(description = "Bitiş tarihi (ISO formatında)") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return ResponseEntity.ok(ticketService.getTicketsByIssuedAtBetween(start, end));
    }
    
    @GetMapping("/admin/find-by-type/{type}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Bilet tipine göre biletleri getir", description = "Belirli bir tipteki biletleri listeler (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Biletler başarıyla alındı"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "400", description = "Geçersiz bilet tipi", content = @Content)
    })
    public ResponseEntity<List<TicketDto>> getTicketsByType(
            @Parameter(description = "Bilet tipi", schema = @Schema(implementation = TicketType.class)) 
            @PathVariable TicketType type) {
        return ResponseEntity.ok(ticketService.getTicketsByType(type));
    }
} 