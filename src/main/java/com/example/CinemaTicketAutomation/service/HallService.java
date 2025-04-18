package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.request.HallCreateRequest;
import com.example.CinemaTicketAutomation.dto.response.HallDto;
import com.example.CinemaTicketAutomation.entity.Hall;
import com.example.CinemaTicketAutomation.entity.Movie;
import com.example.CinemaTicketAutomation.entity.Seat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HallService {

    HallDto createHall(HallCreateRequest request);
    Hall updateHall(long id, Hall hall);
    void deleteHall(long hallId);
    Hall getHall(long hallId);
    HallDto getHallDto(long hallId);

    List<Hall> getAllHalls();
    List<Hall> getHallsWithMinimumCapacity(int minCapacity);//belli kapasiteden yüksek salonları
}
