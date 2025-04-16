package com.example.CinemaTicketAutomation.service;

import com.example.CinemaTicketAutomation.dto.request.UserCreateDto;
import com.example.CinemaTicketAutomation.dto.response.UserDto;
import com.example.CinemaTicketAutomation.dto.request.UserUpdateDto;
import com.example.CinemaTicketAutomation.entity.AppUser;
import com.example.CinemaTicketAutomation.entity.enums.Role;

import java.util.List;
import java.util.Optional;

public interface AppUserService {
    // Temel CRUD işlemleri
    UserDto createUser(UserCreateDto userCreateDto);
    UserDto updateUser(Long userId, UserUpdateDto user);
    void deleteUser(Long userId);
    AppUser findUserById(Long userId);
    UserDto getUserById(Long userId);
    Optional<AppUser> findByUsername(String username);
    
    // Admin için özel metodlar
    List<UserDto> getAllUsers();
    List<UserDto> getUsersByRole(Role role);
    List<UserDto> searchUsersByUsername(String username);
    List<UserDto> searchUsersByEmail(String email);
    UserDto changeUserRole(Long userId, Role newRole);
    long getTotalUserCount();
    long getUserCountByRole(Role role);
} 