package com.example.CinemaTicketAutomation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Kullanıcı oluşturma isteği")
public record UserCreateDto(
        @Schema(description = "Kullanıcının kullanıcı adı") String username,
        @Schema(description = "Şifre") String password,
        @Schema(description = "Kullanıcının e-posta adresi") String mail,
        @Schema(description = "Telefon numarası") String phoneNumber,
        @Schema(description = "İsim") String firstname,
        @Schema(description = "Soyisim") String lastname,
        @Schema(description = "Yaş") Integer age
) {}
