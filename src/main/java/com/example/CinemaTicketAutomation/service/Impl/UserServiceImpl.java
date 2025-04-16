package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.request.UserCreateDto;
import com.example.CinemaTicketAutomation.dto.response.UserDto;
import com.example.CinemaTicketAutomation.dto.request.UserUpdateDto;
import com.example.CinemaTicketAutomation.entity.AppUser;
import com.example.CinemaTicketAutomation.entity.enums.Role;
import com.example.CinemaTicketAutomation.repository.AppUserRepository;
import com.example.CinemaTicketAutomation.security.PasswordEncoderConfig;
import com.example.CinemaTicketAutomation.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoderConfig passwordEncoder;

    @Override
    @Transactional
    public UserDto createUser(UserCreateDto request) {
        // Email kontrolü
        if (appUserRepository.existsByMail(request.mail())) {
            throw new IllegalArgumentException("Email already exists: " + request.mail());
        }
        
        // Username kontrolü
        if (appUserRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("Username already exists: " + request.username());
        }

        AppUser appUser = AppUser.builder()
                .username(request.username())
                .password(passwordEncoder.passwordEncoder().encode(request.password()))
                .mail(request.mail())
                .phoneNumber(request.phoneNumber())
                .firstname(request.firstname())
                .lastName(request.lastname())
                .age(request.age())
                .role(Role.ROLE_USER)
                .build();

        AppUser savedUser = appUserRepository.save(appUser);
        return convertToDto(savedUser);
    }

    @Override
    @Transactional
    public UserDto updateUser(Long userId, UserUpdateDto updatedUserDto) {
        AppUser existingAppUser = findUserById(userId);
        
        // Email güncelleme ve kontrolü
        if (updatedUserDto.getMail() != null && !existingAppUser.getMail().equals(updatedUserDto.getMail()) &&
            appUserRepository.existsByMail(updatedUserDto.getMail())) {
            throw new IllegalArgumentException("Email already exists: " + updatedUserDto.getMail());
        }
        
        // Username güncelleme ve kontrolü
        if (updatedUserDto.getUsername() != null && !existingAppUser.getUsername().equals(updatedUserDto.getUsername()) &&
            appUserRepository.existsByUsername(updatedUserDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + updatedUserDto.getUsername());
        }
        
        // Alanları güncelle
        if (updatedUserDto.getUsername() != null) {
            existingAppUser.setUsername(updatedUserDto.getUsername());
        }
        
        if (updatedUserDto.getMail() != null) {
            existingAppUser.setMail(updatedUserDto.getMail());
        }
        
        if (updatedUserDto.getFirstname() != null) {
            existingAppUser.setFirstname(updatedUserDto.getFirstname());
        }
        
        if (updatedUserDto.getLastName() != null) {
            existingAppUser.setLastName(updatedUserDto.getLastName());
        }
        
        if (updatedUserDto.getPhoneNumber() != null) {
            existingAppUser.setPhoneNumber(updatedUserDto.getPhoneNumber());
        }
        
        if (updatedUserDto.getAge() != 0) {
            existingAppUser.setAge(updatedUserDto.getAge());
        }
        
        // Şifre güncelleme (eğer yeni şifre verilmişse)
        if (updatedUserDto.getPassword() != null && !updatedUserDto.getPassword().isEmpty()) {
            existingAppUser.setPassword(passwordEncoder.passwordEncoder().encode(updatedUserDto.getPassword()));
        }
        
        AppUser savedAppUser = appUserRepository.save(existingAppUser);
        return convertToDto(savedAppUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        if (!appUserRepository.existsById(userId)) {
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        appUserRepository.deleteById(userId);
    }

    @Override
    public AppUser findUserById(Long userId) {
        return appUserRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }
    
    @Override
    public UserDto getUserById(Long userId) {
        AppUser appUser = findUserById(userId);
        return convertToDto(appUser);
    }

    @Override
    public Optional<AppUser> findByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return appUserRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getUsersByRole(Role role) {
        return appUserRepository.findByRole(role).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> searchUsersByUsername(String username) {
        return appUserRepository.findByUsernameContainingIgnoreCase(username).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserDto> searchUsersByEmail(String email) {
        return appUserRepository.findByMailContainingIgnoreCase(email).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserDto changeUserRole(Long userId, Role newRole) {
        AppUser appUser = findUserById(userId);
        appUser.setRole(newRole);
        AppUser savedAppUser = appUserRepository.save(appUser);
        return convertToDto(savedAppUser);
    }

    @Override
    public long getTotalUserCount() {
        return appUserRepository.count();
    }

    @Override
    public long getUserCountByRole(Role role) {
        return appUserRepository.countByRole(role);
    }

    // AppUser -> UserDto dönüşümü
    private UserDto convertToDto(AppUser user) {
        return new UserDto(
                user.getId(),
                user.getUsername(),
                user.getMail(),
                user.getPhoneNumber(),
                user.getFirstname(),
                user.getLastName(),
                user.getAge(),
                user.getRole().name()
        );
    }
} 