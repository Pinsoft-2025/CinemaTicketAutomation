package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.dto.response.TicketDto;
import com.example.CinemaTicketAutomation.entity.Ticket;
import com.example.CinemaTicketAutomation.entity.enums.CancellationStatus;
import com.example.CinemaTicketAutomation.entity.enums.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByReservationId(long reservationId);
    List<Ticket> findByReservation_AppUser_Id(long userId);

    List<Ticket> findAll();

    // Yeni metodlar
    Ticket findBySeanceSeat_Id(Long seanceSeatId);
    List<Ticket> findByIssuedAtBetween(LocalDateTime start, LocalDateTime end);
    Ticket findByBarcode(String barcode);
    List<Ticket> findByType(TicketType type);
    List<Ticket> findByCancellationStatus(CancellationStatus status);
    List<Ticket> findByBarcodeAndCancellationStatus(String barcode, CancellationStatus status);
}
