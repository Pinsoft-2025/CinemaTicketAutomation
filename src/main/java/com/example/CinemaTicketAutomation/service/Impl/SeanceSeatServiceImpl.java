package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.response.SeanceSeatDto;
import com.example.CinemaTicketAutomation.entity.Seance;
import com.example.CinemaTicketAutomation.entity.Seat;
import com.example.CinemaTicketAutomation.entity.SeanceSeat;
import com.example.CinemaTicketAutomation.entity.enums.SeatStatus;
import com.example.CinemaTicketAutomation.repository.SeanceSeatRepository;
import com.example.CinemaTicketAutomation.repository.SeanceRepository;
import com.example.CinemaTicketAutomation.repository.SeatRepository;
import com.example.CinemaTicketAutomation.service.SeanceSeatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeanceSeatServiceImpl implements SeanceSeatService {

    private final SeanceSeatRepository seanceSeatRepository;
    private final SeanceRepository seanceRepository;
    private final SeatRepository seatRepository;

    /*
    seat sayısı belli, seance oluştururken o seance için seatlerin kopyalarını oluşturur gibi olacak
    yani initialize edicez

    1- SEANCE BUL: hangi seance için initialize yapıyoruz
    2- SEAT BUL: Seat bilgisi Hall içinde saklıdır. O seance için seçilen Hall'dan doğru Seat listesine ulaşırız.
    3- SEANCESEAT OLUŞTUR: o seance için, bulduğumuz seatlerin bir kopyasını oluştururuz. burda seatler bir blue print gibi davranır.

    artık bir hall'daki her seat, seance başına tekrar satılabilir. Bu işlem yapılmazsa seat 1 defa satılınca her seance için satılık gözükür.
    Ama seance başına seat'in kopyasını oluşturmak (seance-seat) bunu önler.
     */
    @Transactional
    @Override
    public List<SeanceSeatDto> initializeSeanceSeats(long seanceId) {
        Seance seance = seanceRepository.findById(seanceId)
                .orElseThrow(() -> new EntityNotFoundException("Seans bulunamadı: " + seanceId));

        List<Seat> seats = seatRepository.findByHallId(seance.getHall().getId());

        if (seanceSeatRepository.existsBySeanceId(seanceId)) {
            throw new IllegalStateException("Bu seans için koltuklar zaten oluşturulmuş: " + seanceId);
        }

        List<SeanceSeat> seanceSeats = new ArrayList<>();

        for (Seat seat : seats) {
            SeanceSeat seanceSeat = new SeanceSeat();
            seanceSeat.setSeance(seance);
            seanceSeat.setSeat(seat);
            seanceSeat.setStatus(SeatStatus.FREE);
            seanceSeats.add(seanceSeatRepository.save(seanceSeat));
        }

        return seanceSeats.stream().map(this::toDto).collect(Collectors.toList());
    }

    //istenen seance için seat görme
    @Override
    public List<SeanceSeatDto> getSeanceSeatsBySeanceId(long seanceId) {
        if (!seanceRepository.existsById(seanceId)) {
            throw new EntityNotFoundException("Seans bulunamadı: " + seanceId);
        }
        return seanceSeatRepository.findBySeanceId(seanceId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public SeanceSeatDto getSeanceSeat(long seanceSeatId) {
        return toDto(seanceSeatRepository.findById(seanceSeatId)
                .orElseThrow(() -> new EntityNotFoundException("Seans koltuğu bulunamadı: " + seanceSeatId)));
    }

    @Transactional
    @Override
    public SeanceSeatDto updateSeanceSeatStatus(long seanceSeatId, SeatStatus status) {
        SeanceSeat seanceSeat = seanceSeatRepository.findById(seanceSeatId)
                .orElseThrow(() -> new EntityNotFoundException("Seans koltuğu bulunamadı: " + seanceSeatId));

        if (seanceSeat.getStatus() == SeatStatus.TAKEN && status != SeatStatus.TAKEN) {
            throw new IllegalStateException("Satılmış koltuğun durumu değiştirilemez");
        }

        seanceSeat.setStatus(status);
        return toDto(seanceSeatRepository.save(seanceSeat));
    }

    @Override
    public List<SeanceSeatDto> getFreeSeatsForSeance(long seanceId) {
        if (!seanceRepository.existsById(seanceId)) {
            throw new EntityNotFoundException("Seans bulunamadı: " + seanceId);
        }
        return seanceSeatRepository.findBySeanceIdAndStatus(seanceId, SeatStatus.FREE)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /*
    seat seçim için fonk.(alım değil! seçim ekranı)
    istenirse tek seferde birden fazla seat ayırttırılabilir
    bu ekrandan sonra ödeme ekranına geçilir.
     */
    @Transactional
    @Override
    public List<SeanceSeatDto> reserveSeats(List<Long> seanceSeatIds) {
        List<SeanceSeat> reservedSeats = new ArrayList<>();

        for (Long id : seanceSeatIds) {
            SeanceSeat seanceSeat = seanceSeatRepository.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Seans koltuğu bulunamadı: " + id));

            if (seanceSeat.getStatus() != SeatStatus.FREE) {
                throw new IllegalStateException("Koltuk müsait değil: " + id);
            }

            seanceSeat.setStatus(SeatStatus.RESERVED);
            reservedSeats.add(seanceSeatRepository.save(seanceSeat));
        }

        return reservedSeats.stream().map(this::toDto).collect(Collectors.toList());
    }
    
    @Override
    public SeanceSeat findAndValidateSeanceSeat(Long seanceSeatId) {
        SeanceSeat seanceSeat = seanceSeatRepository.findById(seanceSeatId)
                .orElseThrow(() -> new EntityNotFoundException("Seans koltuğu bulunamadı: " + seanceSeatId));

        if (seanceSeat.getStatus() != SeatStatus.RESERVED && seanceSeat.getStatus() != SeatStatus.FREE) {
            throw new IllegalStateException("Koltuk müsait değil: " + seanceSeatId);
        }

        return seanceSeat;
    }

    @Override
    public void updateSeatStatus(SeanceSeat seanceSeat, SeatStatus status) {
        seanceSeat.setStatus(status);
        seanceSeatRepository.save(seanceSeat);
    }

    // Entity -> DTO dönüşüm metodu
    private SeanceSeatDto toDto(SeanceSeat seanceSeat) {
        if (seanceSeat == null) return null;
        
        return new SeanceSeatDto(
            seanceSeat.getId(),
            seanceSeat.getSeance().getId(),
            seanceSeat.getSeat().getId(),
            seanceSeat.getStatus(),
            seanceSeat.getStatus() == SeatStatus.TAKEN
        );
    }
}