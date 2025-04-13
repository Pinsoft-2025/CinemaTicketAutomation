package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.Seance;
import com.example.CinemaTicketAutomation.entity.enums.MovieFormat;
import com.example.CinemaTicketAutomation.repository.HallRepository;
import com.example.CinemaTicketAutomation.repository.MovieRepository;
import com.example.CinemaTicketAutomation.repository.SeanceRepository;
import com.example.CinemaTicketAutomation.service.SeanceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


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
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;

    @Transactional
    @Override
    public Seance createSeance(Seance seance) {
        Movie movie = movieRepository.findById(seance.getMovie().getId())
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + seance.getMovie().getId()));

        Hall hall = hallRepository.findById(seance.getHall().getId())
                .orElseThrow(() -> new EntityNotFoundException("Hall not found with id: " + seance.getHall().getId()));

        calculateEndTime(seance, movie);

         if (!isHallAvailableForTimeSlot(hall.getId(), seance.getDate(), seance.getStartTime(), seance.getEndTime())) {
            throw new IllegalStateException("Hall " + hall.getId() + " is not available for the specified time slot");
        }

        seance.setMovie(movie);
        seance.setHall(hall);

        return seanceRepository.save(seance);
    }

    @Transactional
    @Override
    public Seance updateSeance(Seance seance) {
        Seance existingSeance = seanceRepository.findById(seance.getId())
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + seance.getId()));

        Movie movie = movieRepository.findById(seance.getMovie().getId())
                .orElseThrow(() -> new EntityNotFoundException("Movie not found with id: " + seance.getMovie().getId()));

        Hall hall = hallRepository.findById(seance.getHall().getId())
                .orElseThrow(() -> new EntityNotFoundException("Hall not found with id: " + seance.getHall().getId()));

         calculateEndTime(seance, movie);

        // Check if the hall is available for this time slot (excluding this session)
        boolean hallAvailable = true;

        if (existingSeance.getHall().getId() != seance.getHall().getId() ||
                !existingSeance.getDate().equals(seance.getDate()) ||
                !existingSeance.getStartTime().equals(seance.getStartTime()) ||
                !existingSeance.getEndTime().equals(seance.getEndTime())) {

            hallAvailable = isHallAvailableForTimeSlot(hall.getId(), seance.getDate(), seance.getStartTime(), seance.getEndTime());
        }

        if (!hallAvailable) {
            throw new IllegalStateException("Hall " + hall.getId() + " is not available for the specified time slot");
        }

        existingSeance.setStartTime(seance.getStartTime());
        existingSeance.setBreakTime(seance.getBreakTime());
        existingSeance.setEndTime(seance.getEndTime());
        existingSeance.setDate(seance.getDate());
        existingSeance.setDubLanguage(seance.getDubLanguage());
        existingSeance.setHasSubtitle(seance.isHasSubtitle());
        existingSeance.setSubLanguage(seance.getSubLanguage());
        existingSeance.setFormat(seance.getFormat());
        existingSeance.setMovie(movie);
        existingSeance.setHall(hall);

        return seanceRepository.save(existingSeance);
    }

    @Override
    public void deleteSeance(long seanceId) {
        if (!seanceRepository.existsById(seanceId)) {
            throw new EntityNotFoundException("Session not found with id: " + seanceId);
        }

        seanceRepository.deleteById(seanceId);
    }

    @Override
    public Seance getSeance(long seanceId) {
        return seanceRepository.findById(seanceId)
                .orElseThrow(() -> new EntityNotFoundException("Session not found with id: " + seanceId));
    }

    @Override
    public List<Seance> getAllSeance() {
        return seanceRepository.findAll();
    }

    @Override
    public List<Seance> getSeanceByMovie(long movieId) {
        // Check if the movie exists
        if (!movieRepository.existsById(movieId)) {
            throw new EntityNotFoundException("Movie not found with id: " + movieId);
        }

        return seanceRepository.findByMovieId(movieId);
    }

    @Override
    public List<Seance> getSeanceByHall(long hallId) {
        // Check if the hall exists
        if (!hallRepository.existsById(hallId)) {
            throw new EntityNotFoundException("Hall not found with id: " + hallId);
        }

        return seanceRepository.findByHallId(hallId);
    }

    @Override
    public List<Seance> getSeanceByDate(LocalDate date) {
        return seanceRepository.findByDate(date);
    }

    @Override
    public List<Seance> getAvailableSeanceByDateAndMovieAndFormat(LocalDate date, long movieId, MovieFormat format) {
        // Check if the movie exists
        if (!movieRepository.existsById(movieId)) {
            throw new EntityNotFoundException("Movie not found with id: " + movieId);
        }

        return seanceRepository.findByDateAndMovieIdAndFormat(date, movieId, format);
    }

    @Override
    public boolean isHallAvailableForTimeSlot(long hallId, LocalDate date, LocalTime startTime, LocalTime endTime) {
        if (!hallRepository.existsById(hallId)) {
            throw new EntityNotFoundException("Hall not found with id: " + hallId);
        }

        return !seanceRepository.existsOverlappingSession(hallId, date, startTime, endTime);
    }

    private void calculateEndTime(Seance seance, Movie movie) {
        if (movie != null && seance.getBreakTime() != null && seance.getStartTime() != null) {
            int totalMinutes = movie.getDurationMin() +
                    seance.getBreakTime().getHour() * 60 +
                    seance.getBreakTime().getMinute();
            seance.setEndTime(seance.getStartTime().plusMinutes(totalMinutes));
        }
    }
}