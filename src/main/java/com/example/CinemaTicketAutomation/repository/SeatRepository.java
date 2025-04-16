package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {
    /**
     * Belirli bir salondaki koltukları, önce satıra (alfabetik) sonra sütuna (sayısal)
     * göre sıralayarak son eklenen koltuğu bulur.
     *
     * @param hallId Salon ID'si.
     * @return Opsiyonel olarak bulunan son koltuk.
     */
    @Query("SELECT s FROM Seat s WHERE s.hall.id = :hallId ORDER BY s.row DESC, s.column DESC")
    Optional<Seat> findLastSeatInHall(@Param("hallId") long hallId); // LIMIT 1 otomatik eklenir Optional ile

    // Bu metot kullanılmıyor, ama düzenli sıralama ihtiyacı olursa kullanılabilir
    List<Seat> findByHallIdOrderByRowAscColumnAsc(long hallId);

    /**
     * Belirli bir salonda, belirli bir satır ve sütun numarasına sahip koltuk var mı kontrolü.
     * (uniqueConstraints olsa da emin olmak için kullanılabilir)
     * @param hallId Salon ID
     * @param row Satır
     * @param column Sütun
     * @return Koltuk varsa true, yoksa false
     */
    boolean existsByHallIdAndRowAndColumn(long hallId, String row, int column);

    List<Seat> findByHallId(long hallId);
}
