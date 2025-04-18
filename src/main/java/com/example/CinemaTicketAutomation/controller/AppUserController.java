package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.UserCreateDto;
import com.example.CinemaTicketAutomation.dto.request.UserUpdateDto;
import com.example.CinemaTicketAutomation.dto.response.UserDto;
import com.example.CinemaTicketAutomation.entity.AppUser;
import com.example.CinemaTicketAutomation.entity.enums.Role;
import com.example.CinemaTicketAutomation.service.AppUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Kullanıcı İşlemleri", description = "Kullanıcı yönetimi ve profil işlemleri için API")
public class AppUserController {

    private final AppUserService appUserService;

    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserCreateDto request) {
        UserDto createdUser = appUserService.createUser(request);
        return ResponseEntity.ok(createdUser);
    }

    // Kullanıcı kendi profilini görüntüleyebilir
    @GetMapping("/find-own/{id}")
    @PreAuthorize("hasRole('USER') and @userSecurity.isCurrentUser(#id)")
    @Operation(summary = "Kullanıcı profilini getir", description = "Kullanıcının kendi profilini görüntüler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kullanıcı profili başarıyla alındı"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı", content = @Content)
    })
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(appUserService.getUserById(id));
    }


    // Kullanıcı kendi profilini güncelleyebilir
    @PutMapping("/update-own/{id}")
    @PreAuthorize("hasRole('USER') and @userSecurity.isCurrentUser(#id)")
    @Operation(summary = "Kullanıcı profilini güncelle", description = "Kullanıcının kendi profil bilgilerini günceller")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kullanıcı profili başarıyla güncellendi"),
        @ApiResponse(responseCode = "400", description = "Geçersiz profil bilgileri", content = @Content),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı", content = @Content)
    })
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto updateDto) {
        return ResponseEntity.ok(appUserService.updateUser(id, updateDto));
    }

    //user kendi hesabını silebilir
    @DeleteMapping("/delete-own/{id}")
    @PreAuthorize("hasRole('USER') and @userSecurity.isCurrentUser(#id)")
    @Operation(summary = "Kullanıcı hesabını sil", description = "Kullanıcının kendi hesabını siler")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Kullanıcı hesabı başarıyla silindi"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı", content = @Content)
    })
    public ResponseEntity<Void> deleteOwnUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    // ADMIN YETKİLİ İŞLEMLER
    
    // Admin tüm kullanıcıları listeleyebilir
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Tüm kullanıcıları getir", description = "Sistemdeki tüm kullanıcıları listeler (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kullanıcılar başarıyla alındı"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content)
    })
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(appUserService.getAllUsers());
    }
    
    // Admin kullanıcıyı silebilir
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kullanıcı sil", description = "Sistemdeki bir kullanıcıyı siler (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Kullanıcı başarıyla silindi"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı", content = @Content)
    })
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "Kullanıcı ID", required = true)
            @PathVariable Long id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    // Admin kullanıcının rolünü değiştirebilir
    @PutMapping("/admin/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Kullanıcı rolünü güncelle", description = "Bir kullanıcının rolünü günceller (Sadece Admin)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Kullanıcı rolü başarıyla güncellendi"),
        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı", content = @Content)
    })
    public ResponseEntity<UserDto> updateUserRole(
            @Parameter(description = "Kullanıcı ID", required = true)
            @PathVariable Long id, 
            @Parameter(description = "Yeni rol", required = true)
            @RequestParam Role roleName) {
        return ResponseEntity.ok(appUserService.changeUserRole(id, roleName));
    }
    
    // Admin belirli role sahip kullanıcıları listeleyebilir
    @GetMapping("/admin/find-by-role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable Role role) {
        return ResponseEntity.ok(appUserService.getUsersByRole(role));
    }
    
    // Admin kullanıcı adına göre arama yapabilir
    @GetMapping("/admin/search")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam String username) {
        return ResponseEntity.ok(appUserService.searchUsersByUsername(username));
    }
    
    // Admin email'e göre arama yapabilir
    @GetMapping("/search/email")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> searchUsersByEmail(@RequestParam String email) {
        return ResponseEntity.ok(appUserService.searchUsersByEmail(email));
    }

    // User endpoints

    
//    @PutMapping("/{id}/password")
//    @PreAuthorize("hasRole('USER') and @userSecurity.isCurrentUser(#id)")
//    @Operation(summary = "Kullanıcı şifresini değiştir", description = "Kullanıcının kendi şifresini değiştirir")
//    @ApiResponses(value = {
//        @ApiResponse(responseCode = "200", description = "Kullanıcı şifresi başarıyla değiştirildi"),
//        @ApiResponse(responseCode = "400", description = "Geçersiz şifre bilgileri", content = @Content),
//        @ApiResponse(responseCode = "403", description = "Yetkisiz erişim", content = @Content),
//        @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı", content = @Content)
//    })
//    public ResponseEntity<String> changeUserPassword(
//            @Parameter(description = "Kullanıcı ID", required = true)
//            @PathVariable Long id,
//            @Parameter(description = "Şifre değiştirme bilgileri", required = true)
//            @RequestBody PasswordChangeRequest passwordChangeRequest) {
//        appUserService.changePassword(id, passwordChangeRequest);
//        return ResponseEntity.ok("Şifre başarıyla değiştirildi");
//    }
} 