package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.request.ReservationCreateDto;
import com.example.CinemaTicketAutomation.dto.response.ReservationDto;
import com.example.CinemaTicketAutomation.entity.AppUser;
import com.example.CinemaTicketAutomation.entity.Reservation;
import com.example.CinemaTicketAutomation.entity.enums.PaymentMethod;
import com.example.CinemaTicketAutomation.entity.enums.PaymentStatus;
import com.example.CinemaTicketAutomation.entity.enums.ReservationStatus;

import java.math.BigDecimal;
import java.util.List;

public interface ReservationService {
    ReservationDto getReservationById(long reservationId);
    List<ReservationDto> getReservationsByUserId(long userId);
    ReservationDto updatePaymentStatus(long reservationId, PaymentStatus paymentStatus);
    void deleteReservation(long reservationId);
    ReservationDto createInitialReservation(ReservationCreateDto createDto);
    ReservationDto updateReservationTotalPrice(Long reservationId, BigDecimal totalPrice);

    Reservation createReservation(AppUser user, PaymentMethod paymentMethod);
    void updateReservationPrice(long reservationId, BigDecimal totalPrice);
    void updateReservationStatus(Long reservationId, ReservationStatus status);

    Reservation getReservationEntityById(long reservationId);
}
