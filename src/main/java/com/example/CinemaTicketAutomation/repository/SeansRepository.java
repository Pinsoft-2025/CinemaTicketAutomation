package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeansRepository extends JpaRepository<Seat, Long> {
}
