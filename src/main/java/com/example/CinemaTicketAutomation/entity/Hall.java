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
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "hall_no", nullable = false, unique = true)
    private String hallNo; // 1, 2 de olur ama özel bir isim konmak istenirse konur

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @OneToMany(mappedBy = "hall")
    private List<Seat> seats;

    @OneToMany(mappedBy = "hall")
    private List<Seans> seanses;
}
/*
*********************** sadece not almak için yazdım ***********************
@Column(
        name = "hall_no",      // Veritabanındaki sütun adı
        nullable = false,      // NULL değer kabul etmez
        unique = true,         // Tekil olmasını sağlar
        length = 100,          // Maksimum karakter uzunluğu (String için)
        columnDefinition = "INTEGER DEFAULT 1"  // Varsayılan değer tanımı
        precision = 10, scale = 2 ör: 99999999.99
)

 */