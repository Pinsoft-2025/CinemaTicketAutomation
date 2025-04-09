package com.example.CinemaTicketAutomation.entity;

import com.example.CinemaTicketAutomation.entity.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "seans_seats")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SeansSeat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SeatStatus status;

    @ManyToOne
    @JoinColumn(name = "seans_id")
    private Seans seans;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
}
