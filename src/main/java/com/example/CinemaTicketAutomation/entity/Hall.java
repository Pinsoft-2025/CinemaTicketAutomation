package com.example.CinemaTicketAutomation.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "halls")
@Entity
public class Hall {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "hall_no")
    private int hallNo;

    @Column(name = "capacity")
    private int capacity;

    @OneToMany(mappedBy = "hall")
    private List<Seat> seats;

    @OneToMany(mappedBy = "hall")
    private List<Seans> seanses;
}
