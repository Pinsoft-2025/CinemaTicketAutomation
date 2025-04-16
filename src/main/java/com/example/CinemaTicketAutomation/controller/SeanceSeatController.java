package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.response.SeanceSeatDto;
import com.example.CinemaTicketAutomation.entity.enums.SeatStatus;
import com.example.CinemaTicketAutomation.service.SeanceSeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/seance-seats")
@RequiredArgsConstructor
@Tag(name = "Seans Koltuk İşlemleri", description = "Seans koltuk oluşturma, durumunu güncelleme ve sorgulama işlemleri için API")
public class SeanceSeatController {

    private final SeanceSeatService seanceSeatService;
    
    // Admin yetkili endpoint'ler
    
    @PostMapping("/admin/initialize/{seanceId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Seans koltuklarını başlat", description = "Yeni oluşturulan bir seans için koltukları başlatır")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Seans koltukları başarıyla oluşturuldu"),
        @ApiResponse(responseCode = "400", description = "Koltuklarıyla zaten başlatılmış seans", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Seans bulunamadı", content = @Content)
    })
    public ResponseEntity<List<SeanceSeatDto>> initializeSeanceSeats(
            @Parameter(description = "Seans ID") 
            @PathVariable Long seanceId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(seanceSeatService.initializeSeanceSeats(seanceId));
    }
    
    @PutMapping("/admin/update-status/{seanceSeatId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Koltuk durumunu güncelle", description = "Bir seans koltuğunun durumunu günceller")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Koltuk durumu başarıyla güncellendi"),
        @ApiResponse(responseCode = "400", description = "Satılmış koltuğun durumu değiştirilemez", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Seans koltuğu bulunamadı", content = @Content)
    })
    public ResponseEntity<SeanceSeatDto> updateSeanceSeatStatus(
            @Parameter(description = "Seans Koltuk ID") 
            @PathVariable Long seanceSeatId,
            @Parameter(description = "Koltuk durumu", schema = @Schema(implementation = SeatStatus.class)) 
            @RequestParam SeatStatus status) {
        return ResponseEntity.ok(seanceSeatService.updateSeanceSeatStatus(seanceSeatId, status));
    }
    
    // Hem admin hem kullanıcı için endpoint'ler
    
    @GetMapping("/find-by-seance/{seanceId}")
    @Operation(summary = "Seansa ait koltukları getir", description = "Belirli bir seansa ait tüm koltukları listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Koltuk listesi başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Seans bulunamadı", content = @Content)
    })
    public ResponseEntity<List<SeanceSeatDto>> getSeanceSeatsBySeanceId(
            @Parameter(description = "Seans ID") 
            @PathVariable Long seanceId) {
        return ResponseEntity.ok(seanceSeatService.getSeanceSeatsBySeanceId(seanceId));
    }
    
    @GetMapping("/find-by-id/{seanceSeatId}")
    @Operation(summary = "Koltuk detaylarını getir", description = "Belirli bir seans koltuğunun detaylarını getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Koltuk başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Seans koltuğu bulunamadı", content = @Content)
    })
    public ResponseEntity<SeanceSeatDto> getSeanceSeat(
            @Parameter(description = "Seans Koltuk ID") 
            @PathVariable Long seanceSeatId) {
        return ResponseEntity.ok(seanceSeatService.getSeanceSeat(seanceSeatId));
    }
    
    @GetMapping("/find-free-seats/{seanceId}")
    @Operation(summary = "Boş koltukları getir", description = "Belirli bir seanstaki boş koltukları listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Boş koltuklar başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Seans bulunamadı", content = @Content)
    })
    public ResponseEntity<List<SeanceSeatDto>> getFreeSeatsForSeance(
            @Parameter(description = "Seans ID") 
            @PathVariable Long seanceId) {
        return ResponseEntity.ok(seanceSeatService.getFreeSeatsForSeance(seanceId));
    }
    
    // Kullanıcı yetkili endpoint'ler
    
    @PostMapping("/reserve")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Koltukları rezerve et", description = "Belirtilen koltukları kullanıcı için rezerve eder")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Koltuklar başarıyla rezerve edildi"),
        @ApiResponse(responseCode = "400", description = "Koltuklar müsait değil", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Koltuklar bulunamadı", content = @Content)
    })
    public ResponseEntity<List<SeanceSeatDto>> reserveSeats(
            @Parameter(description = "Rezerve edilecek seans koltuk ID'leri") 
            @RequestBody List<Long> seanceSeatIds) {
        return ResponseEntity.ok(seanceSeatService.reserveSeats(seanceSeatIds));
    }
}
