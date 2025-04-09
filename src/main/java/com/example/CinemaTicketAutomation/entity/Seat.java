package com.example.CinemaTicketAutomation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seats")
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "seat_no", nullable = false)
    private int seatNo;

    @Column(name = "row", nullable = false)
    private String row;

    @Column(name = "column", nullable = false)
    private int column;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;
}

