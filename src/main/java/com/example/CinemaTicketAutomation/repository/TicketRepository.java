package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
