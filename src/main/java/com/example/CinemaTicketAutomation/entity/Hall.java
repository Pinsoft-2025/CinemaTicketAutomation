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

    // Seat düzenini belirleyen alanlar
    @Column(name = "max_row", nullable = false) // Örn: "F" (A'dan F'ye kadar satır olduğunu belirtir)
    private String maxRow;

    @Column(name = "max_col", nullable = false) // Örn: 10 (1'den 10'a kadar sütun olduğunu belirtir)
    private int maxCol;

    // Kapasite kaldırıldı, getCapacity() metodu ile hesaplanacak.

    //eğer hall silinirse içindeki tüm seat nesneleride tamamen silinir
    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Seat> seats;

    @OneToMany(mappedBy = "hall", fetch = FetchType.LAZY)
    private List<Seans> seanses;

    public int getCapacity() {
        return this.seats == null ? 0 : this.seats.size();
    }

    public int getTheoreticalMaxCapacity() {
        if (maxRow == null || maxRow.isEmpty() || maxCol <= 0) {
            return 0;
        }
        // Basitçe A=1, B=2... varsayımıyla
        int rowCount = maxRow.toUpperCase().charAt(0) - 'A' + 1;
        return rowCount * maxCol;
    }
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