package com.example.CinemaTicketAutomation.utils;

import java.util.UUID;

public class BarcodeGenerator {
    
    private BarcodeGenerator() {
        // Utility sınıfı - instance oluşturulmasını engelliyoruz
    }
    
    public static String generateBarcode() {
        // Simple implementation using UUID
        return UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
    }
} 