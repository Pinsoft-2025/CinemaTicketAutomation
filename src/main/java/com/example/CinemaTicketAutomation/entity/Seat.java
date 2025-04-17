package com.example.CinemaTicketAutomation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seats", uniqueConstraints = {@UniqueConstraint(columnNames = {"id", "row", "column"})
})
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "seat_no", nullable = false, length = 10) // column + row
    private String seatNo;

    @Column(name = "row", nullable = false, length = 2)
    private String row;

    @Column(name = "column", nullable = false)
    private int column;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false, updatable = false)
    private Hall hall;
}

