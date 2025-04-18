package com.example.CinemaTicketAutomation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class HallCreateRequest {
    @Schema(description = "Salon numarası", example = "Salon 1", required = true)
    @NotBlank(message = "Salon numarası boş olamaz")
    private String hallNo;

    @Schema(description = "Maksimum sıra (A-Z)", example = "F", required = true)
    @NotBlank(message = "Maksimum sıra boş olamaz")
    @Pattern(regexp = "^[A-Z]$", message = "Maksimum sıra A-Z arasında tek bir harf olmalıdır")
    private String maxRow;

    @Schema(description = "Maksimum sütun sayısı", example = "10", required = true)
    @Min(value = 1, message = "Sütun sayısı en az 1 olmalıdır")
    private int maxCol;
} 