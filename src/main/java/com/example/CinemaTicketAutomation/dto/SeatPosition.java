package com.example.CinemaTicketAutomation.dto;

/**
 * Bir koltuğun satır ve sütun pozisyonunu temsil eden değişmez (immutable) veri nesnesi.
 * Bu kayıt, Seat servisinde koltuk pozisyonlarını belirlemede kullanılır.
 */
public record SeatPosition(
        String row,
        int column
)
{
    @Override
    public String toString() {
        return row + column;
    }
} 