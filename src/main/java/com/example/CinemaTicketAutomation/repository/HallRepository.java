package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {
}
