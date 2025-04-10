package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
