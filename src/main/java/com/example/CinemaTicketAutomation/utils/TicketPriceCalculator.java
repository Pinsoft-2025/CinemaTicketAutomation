package com.example.CinemaTicketAutomation.utils;

import com.example.CinemaTicketAutomation.entity.Seance;
import com.example.CinemaTicketAutomation.entity.enums.TicketType;

import java.math.BigDecimal;

public class TicketPriceCalculator {
    
    private TicketPriceCalculator() {
        // Utility sınıfı - instance oluşturulmasını engelliyoruz
    }
    
    public static BigDecimal calculateTicketPrice(Seance seance, TicketType ticketType) {
        // Base price - in a real application this would come from configuration or database
        BigDecimal basePrice = new BigDecimal("50.00");

        // Apply format multipliers
        switch (seance.getFormat()) {
            case THREE_D:
                basePrice = basePrice.multiply(new BigDecimal("1.5"));
                break;
            case IMAX:
                basePrice = basePrice.multiply(new BigDecimal("2.0"));
                break;
            case DOLBY_ATMOS:
                basePrice = basePrice.multiply(new BigDecimal("1.8"));
                break;
            default:
                break;
        }

        // Apply discount based on ticket type
        switch (ticketType) {
            case STUDENT:
                basePrice = basePrice.multiply(new BigDecimal("0.7"));
                break;
            case CHILD:
                basePrice = basePrice.multiply(new BigDecimal("0.5"));
                break;
            case SENIOR:
                basePrice = basePrice.multiply(new BigDecimal("0.6"));
                break;
            default:
                break;
        }

        return basePrice;
    }
} 