package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.entity.Seat;
import com.example.CinemaTicketAutomation.repository.HallRepository;
import com.example.CinemaTicketAutomation.repository.SeatRepository;
import com.example.CinemaTicketAutomation.service.SeatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.NoPermissionException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;

    /*
    Seat oluşturmak hangi Hall için oluşturulucğına bağlıdır.
    Hall'ın doluluk durumuna göre bir seatNo oluşturulur. Ör: B3 -> B Satır, 3 Sütun
    Seat oluşturulabilmesi her durum için geçerli şart Hall "TheoreticalMaxCapacity"nin dolmamış olması

    DURUM 1: Eğer Hall içinde daha önce Seat oluşturulmamışsa yeni seatNo = "A1"

    DURUM 2: Eğer zaten seat bulunuyorsa ve yeni seat oluşturmak için en son eklenen Seat'in seatNo bakılır.
    Hall'ın maxCol ve maxRow değerlerine de bakılarak bir sonraki seat için seatNo belirlernir.
    TheoreticalMaxCapacity değerini aşmıyorsa oluşturulur.

    DURUM 3: Eğer seat eklendikten sonra silinmesinden oluşan boşluklar varsa ilk oralar dolar
    */
    @Transactional
    @Override
    public Seat addNextSeatToHall(long hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new EntityNotFoundException("Hall not found with id: " + hallId));

        String maxRow = hall.getMaxRow().toUpperCase();
        int maxCol = hall.getMaxCol();

        // Get all existing seats for this hall
        List<Seat> existingSeats = seatRepository.findByHallId(hallId);

        // If no seats exist, create the first one (A1)
        if (existingSeats.isEmpty()) {
            return createSeat(hall, "A", 1);
        }

        // Create a grid to track which positions are filled
        boolean[][] grid = new boolean[26][maxCol + 1]; // 26 letters in alphabet

        // Mark existing seats in the grid
        for (Seat seat : existingSeats) {
            char rowChar = seat.getRow().toUpperCase().charAt(0);
            int rowIndex = rowChar - 'A';
            int colIndex = seat.getColumn();

            if (rowIndex >= 0 && rowIndex < 26 && colIndex >= 1 && colIndex <= maxCol) {
                grid[rowIndex][colIndex] = true;
            }
        }

        // Find the first available gap
        for (int r = 0; r < 26; r++) {
            char rowChar = (char) ('A' + r);
            String rowStr = String.valueOf(rowChar);

            // Don't go beyond the max row
            if (rowChar > maxRow.charAt(0)) {
                throw new RuntimeException("Hall with id " + hallId + " is full. Cannot add more seats.");
            }

            for (int c = 1; c <= maxCol; c++) {
                if (!grid[r][c]) {
                    // Found a gap, create a seat here
                    return createSeat(hall, rowStr, c);
                }
            }
        }

        // If we get here, all possible positions are filled
        throw new RuntimeException("Hall with id " + hallId + " is full. Cannot add more seats.");
    }

    // Helper method to create a new seat
    private Seat createSeat(Hall hall, String row, int column) {
        Seat newSeat = new Seat();
        newSeat.setHall(hall);
        newSeat.setRow(row);
        newSeat.setColumn(column);
        newSeat.setSeatNo(row + column);

        // Double-check to ensure this seat doesn't already exist
        if (seatRepository.existsByHallIdAndRowAndColumn(hall.getId(), row, column)) {
            throw new IllegalStateException("Seat " + newSeat.getSeatNo() + " already exists in hall " + hall.getId());
        }

        return seatRepository.save(newSeat);
    }

    @Transactional
    @Override
    public Seat updateSeat(Seat seat) {
        if (seatRepository.existsByHallIdAndRowAndColumn(seat.getHall().getId(), seat.getRow(), seat.getColumn())) {
            throw new IllegalStateException("Seat " + seat.getSeatNo() + " already exists in hall " + seat.getHall().getId());
        }

        Seat oldSeat = seatRepository.findById(seat.getId())
                .orElseThrow(() -> new EntityNotFoundException("Seat doesn't exist with the id: " + seat.getId()));

        oldSeat.setSeatNo(seat.getSeatNo());
        oldSeat.setRow(seat.getRow());
        oldSeat.setColumn(seat.getColumn());

        return seatRepository.save(oldSeat);
    }

    @Override
    public void deleteSeat(long seatId) {
        seatRepository.deleteById(seatId);
    }


    @Override
    public Seat getSeat(long seatId) {
        return seatRepository.findById(seatId)
                .orElseThrow(() -> new EntityNotFoundException("Seat not found with id: " + seatId));
    }

    @Override
    public List<Seat> getAllSeatsByHallId(long hallId) {
        if (!hallRepository.existsById(hallId)) {
            throw new EntityNotFoundException("Hall not found with id: " + hallId);
        }

        return seatRepository.findByHallId(hallId);
    }

}
