package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.Seat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HallService {

    Hall createHall(Hall hall);
    Hall updateHall(Hall hall);
    void deleteHall(long hallId);
    Hall getHall(long hallId);

    List<Hall> getAllHalls();
    List<Hall> getHallsWithMinimumCapacity(int minCapacity);//belli kapasiteden yüksek salonları
}
