package com.example.CinemaTicketAutomation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seats", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"hall_id", "seat_row", "seat_column"})
})
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "seat_no", nullable = false, length = 10) // column + row
    private String seatNo;

    @Column(name = "seat_row", nullable = false, length = 2)
    private String row;

    @Column(name = "seat_column", nullable = false)
    private int column;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false, updatable = false)
    private Hall hall;
}

