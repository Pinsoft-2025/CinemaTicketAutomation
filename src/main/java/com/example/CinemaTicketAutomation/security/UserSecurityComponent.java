package com.example.CinemaTicketAutomation.security;

import com.example.CinemaTicketAutomation.entity.Reservation;
import com.example.CinemaTicketAutomation.entity.Ticket;
import com.example.CinemaTicketAutomation.repository.ReservationRepository;
import com.example.CinemaTicketAutomation.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Spring Security için EL (Expression Language) ifadelerinde kullanılacak
 * güvenlik kontrolleri içeren yardımcı sınıf.
 */
@Component("userSecurity")
@RequiredArgsConstructor
public class UserSecurityComponent {
    
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
    
    /**
     * Şu anki giriş yapmış kullanıcının ID'sinin, verilen kullanıcı ID'si ile eşleşip eşleşmediğini kontrol eder.
     * @param userId kontrol edilecek kullanıcı ID'si
     * @return eşleşiyorsa true, aksi halde false
     */
    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            return userDetails.getId() == userId;
        }
        
        return false;
    }
    
    /**
     * Kullanıcının belirtilen rezervasyona erişim izni olup olmadığını kontrol eder.
     * @param reservationId rezervasyon ID'si
     * @return erişim izni varsa true, aksi halde false
     */
    public boolean canAccessReservation(Long reservationId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getId();
            
            Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
            if (reservationOpt.isPresent()) {
                return reservationOpt.get().getAppUser().getId() == userId;
            }
        }
        
        return false;
    }
    
    /**
     * Kullanıcının belirtilen bilete erişim izni olup olmadığını kontrol eder.
     * @param ticketId bilet ID'si
     * @return erişim izni varsa true, aksi halde false
     */
    public boolean canAccessTicket(Long ticketId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            Long userId = userDetails.getId();
            
            Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
            if (ticketOpt.isPresent() && ticketOpt.get().getReservation() != null) {
                return ticketOpt.get().getReservation().getAppUser().getId() == userId;
            }
        }
        
        return false;
    }
} 