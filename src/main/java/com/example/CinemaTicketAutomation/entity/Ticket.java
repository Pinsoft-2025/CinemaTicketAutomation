package com.example.CinemaTicketAutomation.entity;

import com.example.CinemaTicketAutomation.entity.enums.CancellationStatus;
import com.example.CinemaTicketAutomation.entity.enums.TicketType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tickets")
@Entity
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TicketType type = TicketType.FULL;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "barcode", unique = true, nullable = false)
    private String barcode;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @OneToOne
    @JoinColumn(name = "seans_seat_id", nullable = false)
    private SeanceSeat seanceSeat;
    
    // İptal ile ilgili yeni alanlar
    @Enumerated(EnumType.STRING)
    @Column(name = "cancellation_status")
    private CancellationStatus cancellationStatus = CancellationStatus.NONE;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    @Column(name = "admin_comment")
    private String adminComment;
}
