package com.example.CinemaTicketAutomation.entity;

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
    @Column(name = "id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TicketType type;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "barcode", unique = true, nullable = false)
    private String barcode;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @ManyToOne
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @OneToOne
    @JoinColumn(name = "seans_seat_id")
    private SeansSeat seansSeat;
}
