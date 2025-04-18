package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.MovieCreateDto;
import com.example.CinemaTicketAutomation.dto.response.MovieDto;
import com.example.CinemaTicketAutomation.entity.enums.Country;
import com.example.CinemaTicketAutomation.entity.enums.Genre;
import com.example.CinemaTicketAutomation.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Film İşlemleri", description = "Film ekleme, güncelleme, silme ve listeleme işlemleri için API")
public class MovieController {

    private final MovieService movieService;

    // Admin yetkileri gerektiren metodlar
    
    @PostMapping(path = "/admin/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Film ekle", description = "Yeni bir film ekler (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Film başarıyla eklendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz film bilgileri", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content)
    })
    public ResponseEntity<MovieDto> createMovie(
            @Parameter(description = "Film bilgileri", required = true)
            @ModelAttribute MovieCreateDto movieCreateDto) {
        return ResponseEntity.ok(movieService.createMovie(movieCreateDto));
    }

    @PostMapping("/admin/update-info/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Film güncelle", description = "Mevcut bir filmi günceller (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Film başarıyla güncellendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz film bilgileri", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Film bulunamadı", content = @Content)
    })
    public ResponseEntity<MovieDto> updateMovie(
            @Parameter(description = "Film ID", required = true)
            @PathVariable Long id, 
            @Parameter(description = "Film bilgileri", required = true)
            @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Film sil", description = "Mevcut bir filmi siler (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Film başarıyla silindi"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Film bulunamadı", content = @Content)
    })
    public ResponseEntity<Void> deleteMovie(
            @Parameter(description = "Film ID", required = true)
            @PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
    
    // Hem user hem admin için metodlar
    
    @GetMapping("/find-all")
    @Operation(summary = "Tüm filmleri getir", description = "Sistemdeki tüm filmleri listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filmler başarıyla alındı")
    })
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    
    @GetMapping("/find-by-id/{id}")
    @Operation(summary = "Film bilgilerini getir", description = "ID'ye göre film bilgilerini getirir")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Film başarıyla alındı"),
        @ApiResponse(responseCode = "404", description = "Film bulunamadı", content = @Content)
    })
    public ResponseEntity<MovieDto> getMovieById(
            @Parameter(description = "Film ID", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Film ara", description = "Başlığa göre film arar")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arama sonuçları başarıyla alındı")
    })
    public ResponseEntity<List<MovieDto>> searchMovies(
            @Parameter(description = "Arama anahtar kelimesi", required = true)
            @RequestParam String keyword) {
        return ResponseEntity.ok(movieService.searchMoviesByTitle(keyword));
    }

    @GetMapping("/find-by-genre")
    @Operation(summary = "Türe göre film getir", description = "Film türüne göre filmleri listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filmler başarıyla alındı")
    })
    public ResponseEntity<List<MovieDto>> getMoviesByGenre(
            @Parameter(description = "Film türü", required = true, schema = @Schema(implementation = Genre.class))
            @RequestParam Genre genre) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }

    @GetMapping("/find-by-director")
    @Operation(summary = "Yönetmene göre film getir", description = "Yönetmen adına göre filmleri listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filmler başarıyla alındı")
    })
    public ResponseEntity<List<MovieDto>> getMoviesByDirector(
            @Parameter(description = "Yönetmen adı", required = true)
            @RequestParam String director) {
        return ResponseEntity.ok(movieService.getMoviesByDirector(director));
    }

    @GetMapping("/find-by-country")
    @Operation(summary = "Ülkeye göre film getir", description = "Ülkeye göre filmleri listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filmler başarıyla alındı")
    })
    public ResponseEntity<List<MovieDto>> getMoviesByCountry(
            @Parameter(description = "Ülke", required = true, schema = @Schema(implementation = Country.class))
            @RequestParam Country country) {
        return ResponseEntity.ok(movieService.getMoviesByCountry(country));
    }

    @GetMapping("/find-by-date-range")
    @Operation(summary = "Tarih aralığına göre film getir", description = "Belirli bir tarih aralığındaki filmleri listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filmler başarıyla alındı"),
        @ApiResponse(responseCode = "400", description = "Geçersiz tarih formatı", content = @Content)
    })
    public ResponseEntity<List<MovieDto>> getMoviesByDateRange(
            @Parameter(description = "Başlangıç tarihi", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Bitiş tarihi", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(movieService.getMoviesByDateRange(startDate, endDate));
    }
    
    @GetMapping("/upcoming")
    @Operation(summary = "Yaklaşan filmleri getir", description = "Vizyona girecek filmleri listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filmler başarıyla alındı")
    })
    public ResponseEntity<List<MovieDto>> getUpcomingMovies() {
        return ResponseEntity.ok(movieService.getUpcomingMovies());
    }
    
    @GetMapping("/find-by-date")
    @Operation(summary = "Tarihe göre film getir", description = "Belirli bir tarihteki filmleri listeler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filmler başarıyla alındı"),
        @ApiResponse(responseCode = "400", description = "Geçersiz tarih formatı", content = @Content)
    })
    public ResponseEntity<List<MovieDto>> getMoviesByReleaseDate(
            @Parameter(description = "Vizyon tarihi", required = true)
            @RequestParam LocalDate date) {
        return ResponseEntity.ok(movieService.getMoviesByReleaseDate(date));
    }

    @GetMapping("/count")
    @Operation(summary = "Film sayısını getir", description = "Sistemdeki toplam film sayısını döndürür")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Film sayısı başarıyla alındı")
    })
    long getMoviesCount() {
        return movieService.getMovieCount();
    }
} 