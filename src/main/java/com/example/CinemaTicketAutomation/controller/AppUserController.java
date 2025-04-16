package com.example.CinemaTicketAutomation.controller;

import com.example.CinemaTicketAutomation.dto.request.UserUpdateDto;
import com.example.CinemaTicketAutomation.dto.response.UserDto;
import com.example.CinemaTicketAutomation.entity.enums.Role;
import com.example.CinemaTicketAutomation.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class AppUserController {

    private final AppUserService appUserService;
    
    // Kullanıcı kendi profilini görüntüleyebilir
    @GetMapping("/find-own/{id}")
    @PreAuthorize("hasRole('USER') and @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(appUserService.getUserById(id));
    }


    // Kullanıcı kendi profilini güncelleyebilir
    @PutMapping("/update-own/{id}")
    @PreAuthorize("hasRole('USER') and @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto updateDto) {
        return ResponseEntity.ok(appUserService.updateUser(id, updateDto));
    }

    //user kendi hesabını silebilir
    @DeleteMapping("/delete-own/{id}")
    @PreAuthorize("hasRole('USER') and @userSecurity.isCurrentUser(#id)")
    public ResponseEntity<Void> deleteOwnUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    // ADMIN YETKİLİ İŞLEMLER
    
    // Admin tüm kullanıcıları listeleyebilir
    @GetMapping("/admin/find-all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(appUserService.getAllUsers());
    }
    
    // Admin kullanıcıyı silebilir
    @DeleteMapping("/admin/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        appUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    // Admin kullanıcının rolünü değiştirebilir
    @PutMapping("/admin/change/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> changeUserRole(@PathVariable Long id, @RequestParam Role newRole) {
        return ResponseEntity.ok(appUserService.changeUserRole(id, newRole));
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

} 