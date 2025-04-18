package com.example.CinemaTicketAutomation.dto.request;

import com.example.CinemaTicketAutomation.entity.enums.Country;
import com.example.CinemaTicketAutomation.entity.enums.Genre;
import com.example.CinemaTicketAutomation.entity.enums.Warning;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

//eskiden record idi. @RequestBody yerine @ModelAttribute kullandığımız için class oldu
@Data
public class MovieCreateDto {
    @Schema(required = true)
    private String title;
    
    @Schema(required = true)
    private String description;
    
    @Schema(required = false, description = "Film afişi (opsiyonel)")
    private MultipartFile poster;
    
    @Schema(required = true)
    private String trailerURI;
    
    @Schema(required = true)
    private int durationMin;
    
    @Schema(required = true)
    private List<Genre> genres;
    
    @Schema(required = true)
    private String director;
    
    @Schema(required = true)
    private Country country;
    
    @Schema(required = true)
    private LocalDate releaseDate;
    
    @Schema(required = true)
    private List<Warning> warnings;
}