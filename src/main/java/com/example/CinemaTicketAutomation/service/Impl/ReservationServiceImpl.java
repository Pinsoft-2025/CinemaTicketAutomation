package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.request.ReservationCreateDto;
import com.example.CinemaTicketAutomation.dto.response.ReservationDto;
import com.example.CinemaTicketAutomation.entity.Reservation;
import com.example.CinemaTicketAutomation.entity.AppUser;
import com.example.CinemaTicketAutomation.entity.enums.PaymentMethod;
import com.example.CinemaTicketAutomation.entity.enums.PaymentStatus;
import com.example.CinemaTicketAutomation.entity.enums.ReservationStatus;
import com.example.CinemaTicketAutomation.repository.ReservationRepository;
import com.example.CinemaTicketAutomation.repository.AppUserRepository;
import com.example.CinemaTicketAutomation.service.ReservationService;
import com.example.CinemaTicketAutomation.service.AppUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final AppUserRepository appUserRepository;
    private final AppUserService appUserService;

    @Override
    public ReservationDto createInitialReservation(ReservationCreateDto createDto) {
        AppUser user = appUserRepository.findById(createDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("Kullanıcı bulunamadı: " + createDto.userId()));

        Reservation reservation = new Reservation();
        reservation.setAppUser(user);
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setPaymentMethod(createDto.paymentMethod());
        reservation.setPaymentStatus(PaymentStatus.PENDING);
        reservation.setTotalPrice(BigDecimal.ZERO);

        return toDto(reservationRepository.save(reservation));
    }

    @Transactional
    @Override
    public ReservationDto updatePaymentStatus(long reservationId, PaymentStatus paymentStatus) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Rezervasyon bulunamadı: " + reservationId));

        reservation.setPaymentStatus(paymentStatus);
        return toDto(reservationRepository.save(reservation));
    }

    @Override
    public void deleteReservation(long reservationId) {
        if (!reservationRepository.existsById(reservationId)) {
            throw new EntityNotFoundException("Rezervasyon bulunamadı: " + reservationId);
        }
        reservationRepository.deleteById(reservationId);
    }
    
    @Override
    public ReservationDto updateReservationTotalPrice(Long reservationId, BigDecimal totalPrice) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Rezervasyon bulunamadı: " + reservationId));
        
        reservation.setTotalPrice(totalPrice);
        return toDto(reservationRepository.save(reservation));
    }

    @Override
    public ReservationDto getReservationById(long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Rezervasyon bulunamadı: " + reservationId));
        return toDto(reservation);
    }

    @Override
    public List<ReservationDto> getReservationsByUserId(long userId) {
        if (!appUserRepository.existsById(userId)) {
            throw new EntityNotFoundException("Kullanıcı bulunamadı: " + userId);
        }
        return reservationRepository.findByAppUser_Id(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Reservation createReservation(AppUser user, PaymentMethod paymentMethod) {
        Reservation reservation = new Reservation();
        reservation.setAppUser(user);
        reservation.setReservationTime(LocalDateTime.now());
        reservation.setPaymentMethod(paymentMethod);
        reservation.setPaymentStatus(PaymentStatus.PENDING);
        reservation.setTotalPrice(BigDecimal.ZERO);
        
        return reservationRepository.save(reservation);
    }
    
    @Override
    public void updateReservationPrice(long reservationId, BigDecimal totalPrice) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Rezervasyon bulunamadı: " + reservationId));
        
        reservation.setTotalPrice(totalPrice);
        reservationRepository.save(reservation);
    }

    @Override
    public Reservation getReservationEntityById(long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Rezervasyon bulunamadı: " + reservationId));
    }

    @Override
    @Transactional
    public void updateReservationStatus(Long reservationId, ReservationStatus status) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new EntityNotFoundException("Rezervasyon bulunamadı: " + reservationId));
        
        reservation.setStatus(status);
        reservationRepository.save(reservation);
    }

    // Entity -> DTO dönüşüm metodu
    private ReservationDto toDto(Reservation reservation) {
        if (reservation == null) return null;
        
        return ReservationDto.builder()
                .id(reservation.getId())
                .reservationTime(reservation.getReservationTime())
                .totalPrice(reservation.getTotalPrice())
                .paymentStatus(reservation.getPaymentStatus())
                .paymentMethod(reservation.getPaymentMethod())
                .user(appUserService.getUserById(reservation.getAppUser().getId()))
                .tickets(new ArrayList<>())
                .build();
    }
}