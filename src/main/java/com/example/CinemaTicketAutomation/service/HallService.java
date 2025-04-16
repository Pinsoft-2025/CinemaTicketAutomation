package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.response.HallDto;
import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.Seat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HallService {

    Hall createHall(Hall hall);
    Hall updateHall(long id, Hall hall);
    void deleteHall(long hallId);
    Hall getHall(long hallId);
    HallDto getHallDto(long hallId);

    List<Hall> getAllHalls();
    List<Hall> getHallsWithMinimumCapacity(int minCapacity);//belli kapasiteden yüksek salonları
}
