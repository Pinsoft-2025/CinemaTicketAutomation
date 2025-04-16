package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.MovieCreateDto;
import com.example.CinemaTicketAutomation.dto.response.MovieDto;
import com.example.CinemaTicketAutomation.entity.enums.Country;
import com.example.CinemaTicketAutomation.entity.enums.Genre;
import com.example.CinemaTicketAutomation.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // Admin yetkileri gerektiren metodlar
    
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MovieDto> createMovie(@RequestBody MovieCreateDto movieCreateDto) {
        return ResponseEntity.ok(movieService.createMovie(movieCreateDto));
    }

    @PostMapping("/update-info/{id}")
    public ResponseEntity<MovieDto> updateMovie(@PathVariable Long id, @RequestBody MovieDto movieDto) {
        return ResponseEntity.ok(movieService.updateMovie(id, movieDto));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
    
    // Hem user hem admin için metodlar
    
    @GetMapping("/find-all")
    public ResponseEntity<List<MovieDto>> getAllMovies() {
        return ResponseEntity.ok(movieService.getAllMovies());
    }
    
    @GetMapping("/find-by-id/{id}")
    public ResponseEntity<MovieDto> getMovieById(@PathVariable Long id) {
        return ResponseEntity.ok(movieService.getMovieById(id));
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<MovieDto>> searchMovies(@RequestParam String keyword) {
        return ResponseEntity.ok(movieService.searchMoviesByTitle(keyword));
    }

    @GetMapping("/find-by-genre")
    public ResponseEntity<List<MovieDto>> getMoviesByGenre(@RequestParam Genre genre) {
        return ResponseEntity.ok(movieService.getMoviesByGenre(genre));
    }

    @GetMapping("/find-by-director")
    public ResponseEntity<List<MovieDto>> getMoviesByDirector(@RequestParam String director) {
        return ResponseEntity.ok(movieService.getMoviesByDirector(director));
    }

    @GetMapping("/find-by-country")
    public ResponseEntity<List<MovieDto>> getMoviesByCountry(@RequestParam Country country) {
        return ResponseEntity.ok(movieService.getMoviesByCountry(country));
    }

    @GetMapping("/find-by-date-range")
    public ResponseEntity<List<MovieDto>> getMoviesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(movieService.getMoviesByDateRange(startDate, endDate));
    }
    
    @GetMapping("/upcoming")
    public ResponseEntity<List<MovieDto>> getUpcomingMovies() {
        return ResponseEntity.ok(movieService.getUpcomingMovies());
    }
    
    @GetMapping("/find-by-date")
    public ResponseEntity<List<MovieDto>> getMoviesByReleaseDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(movieService.getMoviesByReleaseDate(date));
    }

    @GetMapping("/count")
    long getMoviesCount() {
        return movieService.getMovieCount();
    }
} 