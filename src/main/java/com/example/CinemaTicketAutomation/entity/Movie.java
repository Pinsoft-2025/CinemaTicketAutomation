package com.example.CinemaTicketAutomation.entity;

import com.example.CinemaTicketAutomation.entity.enums.Country;
import com.example.CinemaTicketAutomation.entity.enums.Genre;
import com.example.CinemaTicketAutomation.entity.enums.Warning;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "poster", length = 100000)
    private String poster;

    @Column(name = "trailer_uri")
    private String trailerURI;

    @Column(name = "duration_min", nullable = false)
    private int durationMin;

    @ElementCollection
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "genre")
    private List<Genre> genres;

    @Column(name = "director")
    private String director;

    @Enumerated(EnumType.STRING)
    @Column(name = "country")
    private Country country;

    @Column(name = "release_date", nullable = false)
    private LocalDate releaseDate;

    @ElementCollection
    @CollectionTable(name = "movie_warnings", joinColumns = @JoinColumn(name = "movie_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "warning")
    private List<Warning> warnings;

    @OneToMany(mappedBy = "movie")
    private List<Seance> seances;
}
