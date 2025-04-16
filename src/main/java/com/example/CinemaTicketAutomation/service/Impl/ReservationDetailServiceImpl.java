package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.response.ReservationWithTicketsDto;
import com.example.CinemaTicketAutomation.entity.Reservation;
import com.example.CinemaTicketAutomation.service.ReservationDetailService;
import com.example.CinemaTicketAutomation.service.ReservationService;
import com.example.CinemaTicketAutomation.service.TicketService;
import com.example.CinemaTicketAutomation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationDetailServiceImpl implements ReservationDetailService {

    private final ReservationService reservationService;
    private final TicketService ticketService;
    private final UserService userService;
    
    @Override
    public ReservationWithTicketsDto getReservationWithTickets(long reservationId) {
        Reservation reservation = reservationService.getReservationEntityById(reservationId);
        
        return ReservationWithTicketsDto.builder()
                .id(reservation.getId())
                .reservationTime(reservation.getReservationTime())
                .totalPrice(reservation.getTotalPrice())
                .paymentStatus(reservation.getPaymentStatus())
                .paymentMethod(reservation.getPaymentMethod())
                .status(reservation.getStatus())
                .user(userService.getUserById(reservation.getAppUser().getId()))
                .tickets(ticketService.getTicketsByReservationId(reservation.getId()))
                .build();
    }
    
    @Override
    public List<ReservationWithTicketsDto> getReservationsWithTicketsByUserId(long userId) {
        // Kullanıcının varlığını kontrol et
        userService.getUserById(userId);
        
        // Kullanıcının tüm rezervasyonlarını getir
        List<Reservation> reservations = reservationService.getReservationsByUserId(userId)
                .stream()
                .map(dto -> reservationService.getReservationEntityById(dto.getId()))
                .collect(Collectors.toList());
        
        // Her rezervasyon için biletleri de dahil ederek DTO'ları oluştur
        return reservations.stream()
                .map(reservation -> ReservationWithTicketsDto.builder()
                        .id(reservation.getId())
                        .reservationTime(reservation.getReservationTime())
                        .totalPrice(reservation.getTotalPrice())
                        .paymentStatus(reservation.getPaymentStatus())
                        .paymentMethod(reservation.getPaymentMethod())
                        .status(reservation.getStatus())
                        .user(userService.getUserById(reservation.getAppUser().getId()))
                        .tickets(ticketService.getTicketsByReservationId(reservation.getId()))
                        .build())
                .collect(Collectors.toList());
    }
} 