package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.request.SeanceCreateDto;
import com.example.CinemaTicketAutomation.dto.request.SeanceSearchDto;
import com.example.CinemaTicketAutomation.dto.request.TimeSlotCheckDto;
import com.example.CinemaTicketAutomation.dto.response.SeanceDto;
import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.Seance;
import com.example.CinemaTicketAutomation.entity.enums.MovieFormat;
import com.example.CinemaTicketAutomation.repository.SeanceRepository;
import com.example.CinemaTicketAutomation.service.MovieService;
import com.example.CinemaTicketAutomation.service.HallService;
import com.example.CinemaTicketAutomation.service.SeanceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

/*
tricky part about halls is you should make sure these 3 work perfectly for any method:
1- movie should exist (handle movie)
2- handle hall
3- !! check if the time period is available !!

also since only start and break times are known you gotta calculate "end time" accordingly
 */
@RequiredArgsConstructor
@Service
public class SeanceServiceImpl implements SeanceService {

    private final SeanceRepository seanceRepository;
    private final MovieService movieService;
    private final HallService hallService;

    @Transactional
    @Override
    public SeanceDto createSeance(SeanceCreateDto createDto) {
        Movie movie = movieService.getMovieEntity(createDto.movieId());
        Hall hall = hallService.getHall(createDto.hallId());

        Seance seance = new Seance();
        updateSeanceFromDto(seance, createDto, movie, hall);

        if (!isHallAvailable(new TimeSlotCheckDto(
                hall.getId(), seance.getDate(), seance.getStartTime(), seance.getEndTime()))) {
            throw new IllegalStateException("Salon " + hall.getId() + " bu zaman dilimi için müsait değil");
        }

        return toDto(seanceRepository.save(seance));
    }

    @Transactional
    @Override
    public SeanceDto updateSeance(Long seanceId, SeanceCreateDto updateDto) {
        // 1. Var olan seansı bul
        Seance existingSeance = findSeanceById(seanceId);
        
        // 2. Gerekli entity'leri al
        Movie movie = movieService.getMovieEntity(updateDto.movieId());
        Hall hall = hallService.getHall(updateDto.hallId());
        
        // 3. Seansı güncelle
        updateSeanceFromDto(existingSeance, updateDto, movie, hall);
        
        // 4. Salon müsait mi kontrol et
        validateHallAvailability(existingSeance, updateDto);
        
        // 5. Kaydet ve dönüştür
        return toDto(seanceRepository.save(existingSeance));
    }

    @Override
    public void deleteSeance(long seanceId) {
        if (!seanceRepository.existsById(seanceId)) {
            throw new EntityNotFoundException("Seans bulunamadı: " + seanceId);
        }
        seanceRepository.deleteById(seanceId);
    }

    @Override
    public SeanceDto getSeance(long seanceId) {
        return toDto(findSeanceById(seanceId));
    }

    @Override
    public List<SeanceDto> getAllSeance() {
        return seanceRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> getSeanceByMovie(long movieId) {
        movieService.getMovieById(movieId); // Film var mı kontrol et
        return seanceRepository.findByMovieId(movieId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> getSeanceByHall(long hallId) {
        hallService.getHallDto(hallId); // Salon var mı kontrol et
        return seanceRepository.findByHallId(hallId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> getSeanceByDate(LocalDate date) {
        return seanceRepository.findByDate(date).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SeanceDto> findAvailableSeances(SeanceSearchDto searchCriteria) {
        movieService.getMovieById(searchCriteria.movieId()); // Film var mı kontrol et
        return seanceRepository.findByDateAndMovieIdAndFormat(
                searchCriteria.date(), 
                searchCriteria.movieId(), 
                searchCriteria.format()
            ).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    @Override
    public boolean isHallAvailable(TimeSlotCheckDto timeSlot) {
        hallService.getHallDto(timeSlot.hallId()); // Salon var mı kontrol et
        return !seanceRepository.existsOverlappingSession(
            timeSlot.hallId(), 
            timeSlot.date(), 
            timeSlot.startTime(), 
            timeSlot.endTime()
        );
    }

    // Helper metodlar
    private Seance findSeanceById(Long seanceId) {
        return seanceRepository.findById(seanceId)
                .orElseThrow(() -> new EntityNotFoundException("Seans bulunamadı: " + seanceId));
    }

    private void updateSeanceFromDto(Seance seance, SeanceCreateDto dto, Movie movie, Hall hall) {
        seance.setStartTime(dto.startTime());
        seance.setBreakTime(dto.breakTime());
        seance.setDate(dto.date());
        seance.setDubLanguage(dto.dubLanguage());
        seance.setHasSubtitle(dto.hasSubtitle());
        seance.setSubLanguage(dto.subLanguage());
        seance.setFormat(dto.format());
        seance.setMovie(movie);
        seance.setHall(hall);
        
        calculateEndTime(seance, movie);
    }

    private void validateHallAvailability(Seance seance, SeanceCreateDto dto) {
        boolean needsAvailabilityCheck = seance.getHall().getId() != dto.hallId() ||
                !seance.getDate().equals(dto.date()) ||
                !seance.getStartTime().equals(dto.startTime());
        
        if (needsAvailabilityCheck) {
            TimeSlotCheckDto timeSlot = new TimeSlotCheckDto(
                seance.getHall().getId(),
                seance.getDate(),
                seance.getStartTime(),
                seance.getEndTime()
            );
            
            if (!isHallAvailable(timeSlot)) {
                throw new IllegalStateException("Salon " + seance.getHall().getId() + 
                        " bu zaman dilimi için müsait değil");
            }
        }
    }

    private void calculateEndTime(Seance seance, Movie movie) {
        if (movie != null && seance.getBreakTime() != null && seance.getStartTime() != null) {
            int totalMinutes = movie.getDurationMin() +
                    seance.getBreakTime().getHour() * 60 +
                    seance.getBreakTime().getMinute();
            seance.setEndTime(seance.getStartTime().plusMinutes(totalMinutes));
        }
    }

    private SeanceDto toDto(Seance seance) {
        if (seance == null) return null;

        return SeanceDto.builder()
                .id(seance.getId())
                .startTime(seance.getStartTime())
                .breakTime(seance.getBreakTime())
                .endTime(seance.getEndTime())
                .date(seance.getDate())
                .dubLanguage(seance.getDubLanguage())
                .hasSubtitle(seance.isHasSubtitle())
                .subLanguage(seance.getSubLanguage())
                .format(seance.getFormat())
                .movie(movieService.getMovieById(seance.getMovie().getId()))
                .hall(hallService.getHallDto(seance.getHall().getId()))
                .build();
    }
}