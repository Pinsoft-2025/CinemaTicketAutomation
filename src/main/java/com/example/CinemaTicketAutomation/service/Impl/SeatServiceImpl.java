package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.response.SeatDto;
import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.entity.Seat;
import com.example.CinemaTicketAutomation.dto.SeatPosition;
import com.example.CinemaTicketAutomation.repository.HallRepository;
import com.example.CinemaTicketAutomation.repository.SeatRepository;
import com.example.CinemaTicketAutomation.service.SeatService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

        List<Seat> existingSeats = seatRepository.findByHallId(hallId);
        if (existingSeats.isEmpty()) {
            return createSeat(hall, "A", 1);
        }

        SeatPosition nextPosition = findNextAvailablePosition(hall, existingSeats);

        return createSeat(hall, nextPosition.row(), nextPosition.column());
    }

    // sonraki boş pozisyonu bulan yardımcı metod
    private SeatPosition findNextAvailablePosition(Hall hall, List<Seat> existingSeats) {
        String maxRow = hall.getMaxRow().toUpperCase();
        int maxCol = hall.getMaxCol();

        int maxRowCount = maxRow.charAt(0) - 'A' + 1;

        boolean[][] grid = createAndFillGrid(existingSeats, maxRowCount, maxCol);

        for (int r = 0; r < maxRowCount; r++) {
            char rowChar = (char) ('A' + r);
            String rowStr = String.valueOf(rowChar);

            for (int c = 1; c <= maxCol; c++) {
                if (!grid[r][c]) {
                    return new SeatPosition(rowStr, c);
                }
            }
        }

        throw new RuntimeException("Salon " + hall.getId() + " dolu. Daha fazla koltuk eklenemez.");
    }

    // Grid oluşturup dolduran yardımcı metod
    private boolean[][] createAndFillGrid(List<Seat> existingSeats, int maxRowCount, int maxCol) {
        boolean[][] grid = new boolean[maxRowCount][maxCol + 1];

        for (Seat seat : existingSeats) {
            char rowChar = seat.getRow().toUpperCase().charAt(0);
            int rowIndex = rowChar - 'A';
            int colIndex = seat.getColumn();

            if (rowIndex >= 0 && rowIndex < maxRowCount && colIndex >= 1 && colIndex <= maxCol) {
                grid[rowIndex][colIndex] = true;
            }
        }

        return grid;
    }

    // Yeni koltuk oluşturan yardımcı metod
    private Seat createSeat(Hall hall, String row, int column) {
        if (seatRepository.existsByHallIdAndRowAndColumn(hall.getId(), row, column)) {
            throw new IllegalStateException(
                row + column + " konumunda zaten bir koltuk var (Salon: " + hall.getId() + ")");
        }

        Seat newSeat = new Seat();
        newSeat.setHall(hall);
        newSeat.setRow(row);
        newSeat.setColumn(column);
        newSeat.setSeatNo(row + column);

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

    @Override
    public SeatDto getSeatDto(long seatId) {
        return toDto(getSeat(seatId));
    }

    @Override
    public List<SeatDto> getAllSeatDtosByHallId(long hallId) {
        return getAllSeatsByHallId(hallId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private SeatDto toDto(Seat seat) {
        if (seat == null) return null;
        
        return SeatDto.builder()
                .seatNo(seat.getSeatNo())
                .row(seat.getRow())
                .column(seat.getColumn())
                .hallId(seat.getHall().getId())
                .build();
    }
}
