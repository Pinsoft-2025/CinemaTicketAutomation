package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.request.MovieCreateDto;
import com.example.CinemaTicketAutomation.dto.response.MovieDto;
import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.enums.Country;
import com.example.CinemaTicketAutomation.entity.enums.Genre;
import com.example.CinemaTicketAutomation.repository.MovieRepository;
import com.example.CinemaTicketAutomation.service.MovieService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    @Transactional
    public MovieDto createMovie(MovieCreateDto movieCreateDto) {
        try {
            Movie movie = createDtoToEntity(movieCreateDto);
            Movie savedMovie = movieRepository.save(movie);
            return toDto(savedMovie);
        } catch (IOException e) {
            throw new RuntimeException("Poster dönüştürme hatası: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MovieDto updateMovie(Long movieId, MovieDto movieDto) {
        if (!movieRepository.existsById(movieId)) {
            throw new EntityNotFoundException("Film bulunamadı: " + movieId);
        }

        // Önce varolan posteri almak için filmi veritabanından çekelim
        Movie existingMovie = movieRepository.findById(movieId)
            .orElseThrow(() -> new EntityNotFoundException("Film bulunamadı: " + movieId));
        
        Movie movie = toEntity(movieDto);
        movie.setId(movieId); // ID'yi ayarla
        
        // Eğer yeni poster yoksa, var olan posteri koru
        if (movieDto.posterBase64() == null || movieDto.posterBase64().isEmpty()) {
            movie.setPoster(existingMovie.getPoster());
        }
        
        Movie updatedMovie = movieRepository.save(movie);
        return toDto(updatedMovie);
    }

    @Override
    @Transactional
    public void deleteMovie(Long movieId) {
        if (!movieRepository.existsById(movieId)) {
            throw new EntityNotFoundException("Film bulunamadı: " + movieId);
        }
        movieRepository.deleteById(movieId);
    }

    @Override
    public MovieDto getMovieById(Long movieId) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Film bulunamadı: " + movieId));
        return toDto(movie);
    }

    //for other services
    @Override
    public Movie getMovieEntity(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new EntityNotFoundException("Film bulunamadı: " + movieId));
    }

    @Override
    public List<MovieDto> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> getMoviesByGenre(Genre genre) {
        return movieRepository.findByGenresContaining(genre).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> getUpcomingMovies() {
        LocalDate now = LocalDate.now();
        return movieRepository.findByReleaseDateAfter(now).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> getMoviesByReleaseDate(LocalDate releaseDate) {
        return movieRepository.findByReleaseDate(releaseDate).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> searchMoviesByTitle(String keyword) {
        return movieRepository.findByTitleContainingIgnoreCase(keyword).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> getMoviesByDirector(String director) {
        return movieRepository.findByDirector(director).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> getMoviesByCountry(Country country) {
        return movieRepository.findByCountry(country).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MovieDto> getMoviesByDateRange(LocalDate startDate, LocalDate endDate) {
        return movieRepository.findByReleaseDateBetween(startDate, endDate, null).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public long getMovieCount() {
        return movieRepository.count();
    }
    
    /**
     * MultipartFile'ı Base64 stringine çevirir
     * @param file Dönüştürülecek dosya
     * @return Base64 formatında string
     */
    private String encodeFileToBase64(MultipartFile file) {
        try {
            return Base64.getEncoder().encodeToString(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Dosya Base64'e dönüştürülürken hata oluştu", e);
        }
    }
    
    // Entity <-> DTO dönüşüm metotları
    private MovieDto toDto(Movie movie) {
        if (movie == null) {
            return null;
        }
        
        return new MovieDto(
            movie.getId(),
            movie.getTitle(),
            movie.getDescription(),
            movie.getPoster(),
            movie.getTrailerURI(),
            movie.getDurationMin(),
            movie.getGenres(),
            movie.getDirector(),
            movie.getCountry(),
            movie.getReleaseDate(),
            movie.getWarnings()
        );
    }
    
    private Movie toEntity(MovieDto dto) {
        if (dto == null) {
            return null;
        }
        
        Movie movie = new Movie();
        movie.setId(dto.id());
        movie.setTitle(dto.title());
        movie.setDescription(dto.description());
        movie.setPoster(dto.posterBase64());
        movie.setTrailerURI(dto.trailerURI());
        movie.setDurationMin(dto.durationMin());
        movie.setGenres(dto.genres());
        movie.setDirector(dto.director());
        movie.setCountry(dto.country());
        movie.setReleaseDate(dto.releaseDate());
        movie.setWarnings(dto.warnings());
        
        return movie;
    }
    
    private Movie createDtoToEntity(MovieCreateDto dto) throws IOException {
        String posterBase64 = null;
        if (dto.getPoster() != null && !dto.getPoster().isEmpty()) {
            try {
                byte[] fileContent = dto.getPoster().getBytes();
                posterBase64 = Base64.getEncoder().encodeToString(fileContent);
            } catch (IOException e) {
                throw new IOException("Poster dosyası işlenirken hata oluştu: " + e.getMessage());
            }
        }

        return Movie.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .director(dto.getDirector())
                .durationMin(dto.getDurationMin())
                .releaseDate(dto.getReleaseDate())
                .poster(posterBase64)
                .trailerURI(dto.getTrailerURI())
                .genres(dto.getGenres())
                .country(dto.getCountry())
                .warnings(dto.getWarnings())
                .build();
    }
}
