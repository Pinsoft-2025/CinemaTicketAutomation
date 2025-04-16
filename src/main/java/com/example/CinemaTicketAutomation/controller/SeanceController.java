package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.SeanceCreateDto;
import com.example.CinemaTicketAutomation.dto.request.SeanceSearchDto;
import com.example.CinemaTicketAutomation.dto.response.SeanceDto;
import com.example.CinemaTicketAutomation.dto.response.SeanceSeatDto;
import com.example.CinemaTicketAutomation.service.SeanceService;
import com.example.CinemaTicketAutomation.service.SeanceSeatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Seans İşlemleri", description = "Seans ekleme, güncelleme, silme ve sorgulama işlemleri için API")
public class SeanceController {

    private final SeanceService seanceService;

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Seans oluştur", description = "Yeni bir seans oluşturur (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seans başarıyla oluşturuldu"),
        @ApiResponse(responseCode = "400", description = "Geçersiz seans bilgileri veya salon müsait değil", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Film veya salon bulunamadı", content = @Content)
    })
    public ResponseEntity<SeanceDto> createSeance(
            @Parameter(description = "Seans bilgileri", required = true)
            @RequestBody SeanceCreateDto seanceCreateDto) {
        return ResponseEntity.ok(seanceService.createSeance(seanceCreateDto));
    }

    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Seans güncelle", description = "Mevcut bir seansı günceller (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seans başarıyla güncellendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz seans bilgileri veya salon müsait değil", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Seans, film veya salon bulunamadı", content = @Content)
    })
    public ResponseEntity<SeanceDto> updateSeance(
            @Parameter(description = "Seans ID", required = true)
            @PathVariable Long id, 
            @Parameter(description = "Seans bilgileri", required = true)
            @RequestBody SeanceCreateDto seanceDto) {
        return ResponseEntity.ok(seanceService.updateSeance(id, seanceDto));
    }

    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Seans sil", description = "Bir seansı siler (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Seans başarıyla silindi"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Seans bulunamadı", content = @Content)
    })
    public ResponseEntity<Void> deleteSeance(
            @Parameter(description = "Seans ID", required = true)
            @PathVariable Long id) {
        seanceService.deleteSeance(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-all")
    @Operation(summary = "Tüm seansları getir", description = "Sistemdeki tüm seansları listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seanslar başarıyla alındı")
    })
    public ResponseEntity<List<SeanceDto>> getAllSeances() {
        return ResponseEntity.ok(seanceService.getAllSeance());
    }

    @GetMapping("/find-by-id/{id}")
    @Operation(summary = "Seans bilgilerini getir", description = "ID'ye göre seans bilgilerini getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seans başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Seans bulunamadı", content = @Content)
    })
    public ResponseEntity<SeanceDto> getSeanceById(
            @Parameter(description = "Seans ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(seanceService.getSeance(id));
    }

    @GetMapping("/find-by-movie/{movieId}")
    @Operation(summary = "Filme göre seansları getir", description = "Belirli bir filme ait tüm seansları listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seanslar başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Film bulunamadı", content = @Content)
    })
    public ResponseEntity<List<SeanceDto>> getSeancesByMovie(
            @Parameter(description = "Film ID", required = true)
            @PathVariable Long movieId) {
        return ResponseEntity.ok(seanceService.getSeanceByMovie(movieId));
    }

    @GetMapping("/find-by-date")
    @Operation(summary = "Tarihe göre seansları getir", description = "Belirli bir tarihteki tüm seansları listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seanslar başarıyla alındı"),
        @ApiResponse(responseCode = "400", description = "Geçersiz tarih formatı", content = @Content)
    })
    public ResponseEntity<List<SeanceDto>> getSeancesByDate(
            @Parameter(description = "Tarih (ISO formatında)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(seanceService.getSeanceByDate(date));
    }

    @GetMapping("/find-by-hall/{hallId}")
    @Operation(summary = "Salona göre seansları getir", description = "Belirli bir salondaki tüm seansları listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Seanslar başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Salon bulunamadı", content = @Content)
    })
    public ResponseEntity<List<SeanceDto>> getSeancesByHall(
            @Parameter(description = "Salon ID", required = true)
            @PathVariable Long hallId) {
        return ResponseEntity.ok(seanceService.getSeanceByHall(hallId));
    }

    @PostMapping("/search")
    @Operation(summary = "Seans ara", description = "Belirli kriterlere göre seansları arar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arama sonuçları başarıyla alındı"),
        @ApiResponse(responseCode = "400", description = "Geçersiz arama kriterleri", content = @Content)
    })
    public ResponseEntity<List<SeanceDto>> searchSeances(
            @Parameter(description = "Arama kriterleri", required = true)
            @RequestBody SeanceSearchDto searchDto) {
        return ResponseEntity.ok(seanceService.findAvailableSeances(searchDto));
    }
}