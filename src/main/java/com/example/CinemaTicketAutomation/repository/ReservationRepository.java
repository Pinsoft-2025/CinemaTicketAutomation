package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByAppUser_Id(long userId);
}
