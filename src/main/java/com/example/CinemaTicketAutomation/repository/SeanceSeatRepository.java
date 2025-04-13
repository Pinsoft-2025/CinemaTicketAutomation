package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.SeanceSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeanceSeatRepository extends JpaRepository<SeanceSeat, Long> {
}
