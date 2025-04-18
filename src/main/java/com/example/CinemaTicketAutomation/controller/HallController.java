package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.HallCreateRequest;
import com.example.CinemaTicketAutomation.dto.response.HallDto;
import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.service.HallService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
@RequiredArgsConstructor
@Tag(name = "Salon İşlemleri", description = "Salon ekleme, güncelleme, silme ve listeleme işlemleri için API")
public class HallController {

    private final HallService hallService;

    @PostMapping("/admin/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Salon ekle", description = "Yeni bir salon ekler (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salon başarıyla eklendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz salon bilgileri", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content)
    })
    public ResponseEntity<HallDto> createHall(
            @Parameter(description = "Salon bilgileri", required = true)
            @Valid @RequestBody HallCreateRequest request) {
        return ResponseEntity.ok(hallService.createHall(request));
    }

    // Admin salon güncelleyebilir
    @PutMapping("/admin/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Salon güncelle", description = "Varolan bir salonu günceller (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salon başarıyla güncellendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz salon bilgileri", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Salon bulunamadı", content = @Content)
    })
    public ResponseEntity<Hall> updateHall(
            @Parameter(description = "Salon ID", required = true)
            @PathVariable Long id, 
            @Parameter(description = "Salon bilgileri", required = true)
            @RequestBody Hall hall) {
        return ResponseEntity.ok(hallService.updateHall(id, hall));
    }

    // Admin salon silebilir
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Salon sil", description = "Varolan bir salonu siler (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Salon başarıyla silindi"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Salon bulunamadı", content = @Content)
    })
    public ResponseEntity<Void> deleteHall(
            @Parameter(description = "Salon ID", required = true)
            @PathVariable Long id) {
        hallService.deleteHall(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/find-hall/{id}")
    @Operation(summary = "Salon bilgilerini getir", description = "ID'sine göre salon bilgilerini getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salon başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Salon bulunamadı", content = @Content)
    })
    public ResponseEntity<Hall> getHall(
            @Parameter(description = "Salon ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(hallService.getHall(id));
    }

    @GetMapping("/find-all")
    @Operation(summary = "Tüm salonları getir", description = "Sistemdeki tüm salonları listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salonlar başarıyla alındı")
    })
    public ResponseEntity<List<Hall>> getAllHalls() {
        return ResponseEntity.ok(hallService.getAllHalls());
    }

    @GetMapping("/halls/capacity")
    @Operation(summary = "Minimum kapasiteli salonları getir", description = "Belirtilen minimum kapasiteye sahip salonları listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Salonlar başarıyla alındı")
    })
    public ResponseEntity<List<Hall>> getHallsWithMinCapacity(
            @Parameter(description = "Minimum kapasite", required = true)
            @RequestParam int minCapacity) {
        return ResponseEntity.ok(hallService.getHallsWithMinimumCapacity(minCapacity));
    }
}