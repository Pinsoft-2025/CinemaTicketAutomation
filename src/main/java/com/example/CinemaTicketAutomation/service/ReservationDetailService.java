package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.response.ReservationWithTicketsDto;
import java.util.List;

/**
 * Rezervasyonları ve biletlerini birlikte yönetmek için servis.
 * Bu servis, ReservationService ve TicketService'in dairesel bağımlılık sorununu çözmek için oluşturulmuştur.
 */
public interface ReservationDetailService {
    
    /**
     * Verilen ID ile rezervasyonu ve bağlı biletleri birlikte getirir
     */
    ReservationWithTicketsDto getReservationWithTickets(long reservationId);
    
    /**
     * Kullanıcıya ait tüm rezervasyonları ve biletleri birlikte getirir
     */
    List<ReservationWithTicketsDto> getReservationsWithTicketsByUserId(long userId);
} 