package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallRepository extends JpaRepository<Hall, Long> {

    //verilen min kapasiteye eşit veya daha büyük hall döner
    @Query("SELECT h FROM Hall h WHERE size(h.seats) >= :minCapacity")
    List<Hall> findHallsWithMinimumCapacity(@Param("minCapacity") int minCapacity);

}
