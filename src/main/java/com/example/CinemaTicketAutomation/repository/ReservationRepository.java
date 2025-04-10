package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
