package com.example.CinemaTicketAutomation.entity;

import com.example.CinemaTicketAutomation.entity.enums.MovieFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "seanses")
@Entity
public class Seans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "start_time", nullable = false)
    private LocalTime startTime;

    @Column(name = "break_time", nullable = false)
    private LocalTime breakTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "dub_language")
    private String dubLanguage;

    @Column(name = "has_subtitle")
    private boolean hasSubtitle;

    @Column(name = "sub_language")
    private String subLanguage;

    @Enumerated(EnumType.STRING)
    @Column(name = "format")
    private MovieFormat format;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;
}

