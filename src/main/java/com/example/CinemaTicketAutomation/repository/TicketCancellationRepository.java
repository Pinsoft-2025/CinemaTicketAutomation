package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.TicketCancellation;
import com.example.CinemaTicketAutomation.entity.enums.CancellationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketCancellationRepository extends JpaRepository<TicketCancellation, Long> {
    Optional<TicketCancellation> findByTicketIdAndStatus(Long ticketId, CancellationStatus status);
    List<TicketCancellation> findAllByStatus(CancellationStatus status);
    Optional<TicketCancellation> findFirstByTicketIdOrderByRequestDateDesc(Long ticketId);
} 