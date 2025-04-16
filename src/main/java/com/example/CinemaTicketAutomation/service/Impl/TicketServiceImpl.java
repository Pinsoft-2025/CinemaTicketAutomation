package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.request.TicketCreateDto;
import com.example.CinemaTicketAutomation.dto.response.SeanceSeatDto;
import com.example.CinemaTicketAutomation.dto.response.TicketDto;
import com.example.CinemaTicketAutomation.entity.*;
import com.example.CinemaTicketAutomation.entity.enums.SeatStatus;
import com.example.CinemaTicketAutomation.entity.enums.TicketType;
import com.example.CinemaTicketAutomation.repository.TicketRepository;
import com.example.CinemaTicketAutomation.service.*;
import com.example.CinemaTicketAutomation.utils.BarcodeGenerator;
import com.example.CinemaTicketAutomation.utils.TicketPriceCalculator;
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
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final ReservationService reservationService;
    private final SeanceSeatService seanceSeatService;

    @Transactional
    @Override
    public List<TicketDto> createTickets(TicketCreateDto ticketCreateDto) {
        // Validation
        if (ticketCreateDto.seanceSeatIds().size() != ticketCreateDto.ticketTypes().size()) {
            throw new IllegalArgumentException("Koltuk sayısı ve bilet tipi sayısı eşit olmalıdır");
        }

        // Get user
        AppUser appUser = userService.findUserById(ticketCreateDto.userId());

        // Create reservation
        Reservation reservation = reservationService.createReservation(appUser, ticketCreateDto.paymentMethod());

        // Create tickets
        List<Ticket> tickets = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (int i = 0; i < ticketCreateDto.seanceSeatIds().size(); i++) {
            Long seanceSeatId = ticketCreateDto.seanceSeatIds().get(i);
            TicketType ticketType = ticketCreateDto.ticketTypes().get(i);

            // Find and validate seat
            SeanceSeat seanceSeat = seanceSeatService.findAndValidateSeanceSeat(seanceSeatId);

            // Update seat status
            seanceSeatService.updateSeatStatus(seanceSeat, SeatStatus.TAKEN);

            // Calculate price
            BigDecimal price = TicketPriceCalculator.calculateTicketPrice(seanceSeat.getSeance(), ticketType);
            totalPrice = totalPrice.add(price);

            // Create ticket
            Ticket ticket = new Ticket();
            ticket.setSeanceSeat(seanceSeat);
            ticket.setReservation(reservation);
            ticket.setType(ticketType);
            ticket.setPrice(price);
            ticket.setBarcode(BarcodeGenerator.generateBarcode());
            ticket.setIssuedAt(LocalDateTime.now());

            tickets.add(ticketRepository.save(ticket));
        }

        // Update reservation total price
        reservationService.updateReservationPrice(reservation.getId(), totalPrice);

        // Convert to DTOs and return
        return tickets.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public List<TicketDto> getTicketsByReservationId(long reservationId) {
        // Önce rezervasyonun varlığını kontrol et
        reservationService.getReservationById(reservationId);

        List<Ticket> tickets = ticketRepository.findByReservationId(reservationId);
        return tickets.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public TicketDto getTicketById(long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new EntityNotFoundException("Bilet bulunamadı: " + ticketId));
        return toDto(ticket);
    }

    @Override
    public List<TicketDto> getTicketsByUserId(long userId) {
        // Önce kullanıcının varlığını kontrol et
        userService.findUserById(userId);

        List<Ticket> tickets = ticketRepository.findByReservation_AppUser_Id(userId);
        return tickets.stream().map(this::toDto).collect(Collectors.toList());
    }

    // Entity <-> DTO dönüşüm metodu
    private TicketDto toDto(Ticket ticket) {
        if (ticket == null) {
            return null;
        }

        // Map basic seat information
        SeanceSeatDto seanceSeatDto = null;
        String movieTitle = null;
        String hallNo = null;
        String seatNo = null;
        LocalDateTime seanceDateTime = null;
        Long reservationId = null;

        if (ticket.getReservation() != null) {
            reservationId = ticket.getReservation().getId();
        }

        if (ticket.getSeanceSeat() != null) {
            // SeanceSeat bilgisini al
            seanceSeatDto = seanceSeatService.getSeanceSeat(ticket.getSeanceSeat().getId());

            // Ekstra bilgileri çek
            if (ticket.getSeanceSeat().getSeance() != null) {
                if (ticket.getSeanceSeat().getSeance().getMovie() != null) {
                    movieTitle = ticket.getSeanceSeat().getSeance().getMovie().getTitle();
                }

                if (ticket.getSeanceSeat().getSeance().getHall() != null) {
                    hallNo = ticket.getSeanceSeat().getSeance().getHall().getHallNo();
                }

                // Combine seance date and time
                if (ticket.getSeanceSeat().getSeance().getDate() != null &&
                    ticket.getSeanceSeat().getSeance().getStartTime() != null) {
                    seanceDateTime = LocalDateTime.of(
                        ticket.getSeanceSeat().getSeance().getDate(),
                        ticket.getSeanceSeat().getSeance().getStartTime()
                    );
                }
            }

            if (ticket.getSeanceSeat().getSeat() != null) {
                seatNo = ticket.getSeanceSeat().getSeat().getSeatNo();
            }
        }

        return TicketDto.builder()
            .id(ticket.getId())
            .type(ticket.getType())
            .price(ticket.getPrice())
            .barcode(ticket.getBarcode())
            .issuedAt(ticket.getIssuedAt())
            .reservationId(reservationId)
            .seanceSeat(seanceSeatDto)
            .movieTitle(movieTitle)
            .hallNo(hallNo)
            .seatNo(seatNo)
            .seanceDateTime(seanceDateTime)
            .build();
    }
}