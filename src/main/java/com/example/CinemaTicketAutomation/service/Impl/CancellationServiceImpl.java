package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.response.TicketCancellationDto;
import com.example.CinemaTicketAutomation.dto.response.TicketDto;
import com.example.CinemaTicketAutomation.entity.Reservation;
import com.example.CinemaTicketAutomation.entity.TicketCancellation;
import com.example.CinemaTicketAutomation.entity.enums.CancellationStatus;
import com.example.CinemaTicketAutomation.entity.enums.ReservationStatus;
import com.example.CinemaTicketAutomation.repository.TicketCancellationRepository;
import com.example.CinemaTicketAutomation.service.CancellationService;
import com.example.CinemaTicketAutomation.service.EmailService;
import com.example.CinemaTicketAutomation.service.TicketService;
import com.example.CinemaTicketAutomation.service.ReservationDetailService;
import com.example.CinemaTicketAutomation.service.ReservationService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancellationServiceImpl implements CancellationService {
    private final TicketService ticketService;
    private final EmailService emailService;
    private final ReservationDetailService reservationDetailService;
    private final ReservationService reservationService;
    private final TicketCancellationRepository cancellationRepository;

    @Override
    @Transactional
    public Long requestTicketCancellation(Long ticketId, String reason) {
        try {
            TicketDto ticket = ticketService.getTicketById(ticketId);
            
            if (ticket.isCancelled()) {
                throw new IllegalStateException("Bu bilet zaten iptal edilmiş.");
            }
            
            String ticketInfo = createTicketInfo(ticket, reason);

            var reservationDetails = reservationDetailService.getReservationWithTickets(ticket.reservationId());
            String userEmail = reservationDetails.user().mail();
            
            // İptal talebini veritabanına kaydet
            TicketCancellation cancellation = new TicketCancellation();
            cancellation.setTicketId(ticketId);
            cancellation.setReason(reason);
            cancellation.setStatus(CancellationStatus.PENDING);
            cancellation.setRequestDate(LocalDateTime.now());
            TicketCancellation saved = cancellationRepository.save(cancellation);
            
            // Admin'e e-posta gönder
            emailService.sendTicketCancellationRequestEmailToAdmin(userEmail, ticketInfo);
            
            log.info("Bilet iptal talebi oluşturuldu: {} - Bilet ID: {}", saved.getId(), ticketId);
            return saved.getId();
        } catch (Exception e) {
            log.error("Bilet iptal talebi oluşturulurken hata oluştu: {}", e.getMessage());
            throw new RuntimeException("Bilet iptal talebi oluşturulamadı: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public void processAdminResponse(Long cancellationId, boolean approved, String adminNote) {
        try {
            TicketCancellation cancellation = cancellationRepository.findById(cancellationId)
                .orElseThrow(() -> new EntityNotFoundException("İptal talebi bulunamadı: " + cancellationId));
            
            if (cancellation.getStatus() != CancellationStatus.PENDING) {
                throw new IllegalStateException("Bu iptal talebi zaten işlenmiş.");
            }
            
            Long ticketId = cancellation.getTicketId();
            TicketDto ticket = ticketService.getTicketById(ticketId);

            var reservationDetails = reservationDetailService.getReservationWithTickets(ticket.reservationId());
            String userEmail = reservationDetails.user().mail();
            
            // Kullanıcıya e-posta gönder
            emailService.sendAdminResponseToUser(userEmail, approved);
            
            // İptal talebini güncelle
            cancellation.setStatus(approved ? CancellationStatus.APPROVED : CancellationStatus.REJECTED);
            cancellation.setProcessDate(LocalDateTime.now());
            cancellation.setAdminNote(adminNote);
            cancellationRepository.save(cancellation);
            
            if (approved) {
                // Bileti iptal et
                ticketService.cancelTicket(ticketId);
                
                Long reservationId = ticket.reservationId();
                var allTickets = reservationDetailService.getReservationWithTickets(reservationId).tickets();
                
                // Rezervasyon tutarını güncelle
                BigDecimal cancelledTicketPrice = ticket.price();
                Reservation reservation = reservationService.getReservationEntityById(reservationId);
                BigDecimal newTotalPrice = reservation.getTotalPrice().subtract(cancelledTicketPrice);
                reservationService.updateReservationPrice(reservationId, newTotalPrice);
                
                // Tüm biletler iptal edildiyse rezervasyonu iptal et
                boolean allTicketsCancelled = true;
                for (TicketDto t : allTickets) {
                    if (!t.id().equals(ticketId) && !t.isCancelled()) {
                        allTicketsCancelled = false;
                        break;
                    }
                }
                
                if (allTicketsCancelled) {
                    reservationService.updateReservationStatus(reservationId, ReservationStatus.CANCELLED);
                }
            }
            
            log.info("Bilet iptal talebi işlendi: {} - Durum: {}", cancellationId, cancellation.getStatus());
        } catch (Exception e) {
            log.error("Bilet iptal talebi işlenirken hata oluştu: {}", e.getMessage());
            throw new RuntimeException("Bilet iptal talebi işlenemedi: " + e.getMessage());
        }
    }
    
    @Override
    public List<TicketCancellationDto> getPendingCancellationRequests() {
        return cancellationRepository.findAllByStatus(CancellationStatus.PENDING)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    @Override
    public TicketCancellationDto getCancellationById(Long cancellationId) {
        TicketCancellation cancellation = cancellationRepository.findById(cancellationId)
            .orElseThrow(() -> new EntityNotFoundException("İptal talebi bulunamadı: " + cancellationId));
        return mapToDto(cancellation);
    }
    
    @Override
    public List<TicketCancellationDto> getCancellationsByStatus(CancellationStatus status) {
        return cancellationRepository.findAllByStatus(status)
            .stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }
    
    private TicketCancellationDto mapToDto(TicketCancellation cancellation) {
        TicketDto ticket = ticketService.getTicketById(cancellation.getTicketId());
        return new TicketCancellationDto(
            cancellation.getId(),
            cancellation.getTicketId(),
            cancellation.getReason(),
            cancellation.getStatus(),
            cancellation.getRequestDate(),
            cancellation.getProcessDate(),
            cancellation.getAdminNote(),
            ticket
        );
    }

    private String createTicketInfo(TicketDto ticket, String reason) {
        return String.format("""
            Bilet No: %d
            Film: %s
            Salon: %s
            Koltuk: %s
            Seans: %s
            Fiyat: %s TL
            İptal Nedeni: %s
            """,
            ticket.id(),
            ticket.movieTitle(),
            ticket.hallNo(),
            ticket.seatNo(),
            ticket.seanceDateTime(),
            ticket.price(),
            reason
        );
    }
} 