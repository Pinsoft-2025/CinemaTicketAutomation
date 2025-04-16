package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.response.HallDto;
import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.entity.Seat;
import com.example.CinemaTicketAutomation.repository.HallRepository;
import com.example.CinemaTicketAutomation.service.HallService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;

    //no sensitive info, no need for dto
    @Override
    public Hall createHall(Hall hall) {
        return hallRepository.save(hall);
    }

    /*
    updates hall no, max column number, max row number of the halls.
    tricky scenario is, reducing max row or column changes the number of seats that hall can have.
    if as a result there seem to be more seats then what the new capacity allows, those must get deleted.
    method first checks for any misfit seats in case this scenario happens by storing old values and comparing with new ones
     */
    @Transactional
    @Override
    public Hall updateHall(Hall hall) {
        Hall oldHall = hallRepository.findById(hall.getId())
                .orElseThrow(() -> new EntityNotFoundException("Hall not found"));

        // Update basic properties
        oldHall.setHallNo(hall.getHallNo());

        // Store the old max values for comparison
        String oldMaxRow = oldHall.getMaxRow();
        int oldMaxCol = oldHall.getMaxCol();

        // Update the max dimensions
        oldHall.setMaxRow(hall.getMaxRow());
        oldHall.setMaxCol(hall.getMaxCol());

        // Check if capacity is reduced
        char newMaxRowChar = hall.getMaxRow().toUpperCase().charAt(0);
        char oldMaxRowChar = oldMaxRow.toUpperCase().charAt(0);
        boolean capacityReduced = newMaxRowChar < oldMaxRowChar || hall.getMaxCol() < oldMaxCol;

        // If capacity is reduced, delete seats that are now out of bounds
        if (capacityReduced && oldHall.getSeats() != null) {
            List<Seat> seatsToRetain = oldHall.getSeats().stream()
                    .filter(seat -> {
                        char rowChar = seat.getRow().toUpperCase().charAt(0);
                        int col = seat.getColumn();

                        // Keep only seats that are within the new boundaries
                        return rowChar <= newMaxRowChar && col <= hall.getMaxCol();
                    })
                    .collect(Collectors.toList());

            // Update the seats list (will automatically remove orphaned seats due to orphanRemoval=true)
            oldHall.getSeats().clear();
            oldHall.getSeats().addAll(seatsToRetain);
        }

        return hallRepository.save(oldHall);
    }

    //WARNING: hall silinirse içindeki tüm seat'lerde silinir
    @Transactional
    @Override
    public void deleteHall(long hallId) {
        if (hallRepository.existsById(hallId)) {
            hallRepository.deleteById(hallId);
        }else{
            throw new EntityNotFoundException("Hall not found");
        }
    }

    @Override
    public Hall getHall(long hallId) {
        return hallRepository.findById(hallId).orElseThrow(() -> new EntityNotFoundException("Hall not found"));
    }

    @Override
    public List<Hall> getAllHalls() {
        return hallRepository.findAll().stream().collect(Collectors.toList());
    }

    //performans iyileştirme için @Query kullanıldı
    @Override
    public List<Hall> getHallsWithMinimumCapacity(int minCapacity) {
        return hallRepository.findHallsWithMinimumCapacity(minCapacity);
    }

    @Override
    public HallDto getHallDto(long hallId) {
        Hall hall = getHall(hallId);
        return toDto(hall);
    }

    @Override
    public List<HallDto> getAllHallDtos() {
        return getAllHalls().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private HallDto toDto(Hall hall) {
        if (hall == null) return null;
        return new HallDto(
            hall.getId(),
            hall.getHallNo(),
            hall.getMaxRow(),
            hall.getMaxCol()
        );
    }
}
