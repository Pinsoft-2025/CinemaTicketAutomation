package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.SeanceSeat;
import com.example.CinemaTicketAutomation.entity.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeanceSeatRepository extends JpaRepository<SeanceSeat, Long> {
    List<SeanceSeat> findBySeanceId(long seanceId);
    List<SeanceSeat> findBySeanceIdAndStatus(long seanceId, SeatStatus status);
    boolean existsBySeanceId(long seanceId);
}
