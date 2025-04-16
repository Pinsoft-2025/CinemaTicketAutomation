package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByReservationId(long reservationId);
    List<Ticket> findByReservation_AppUser_Id(long userId);
}
