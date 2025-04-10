package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.SeansSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeansSeatRepository extends JpaRepository<SeansSeat, Long> {
}
