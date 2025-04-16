package com.example.CinemaTicketAutomation.service.Impl;

import com.example.CinemaTicketAutomation.dto.request.AuthRequest;
import com.example.CinemaTicketAutomation.dto.response.AuthResponse;
import com.example.CinemaTicketAutomation.dto.request.RegisterRequest;
import com.example.CinemaTicketAutomation.entity.AppUser;
import com.example.CinemaTicketAutomation.entity.enums.Role;
import com.example.CinemaTicketAutomation.repository.AppUserRepository;
import com.example.CinemaTicketAutomation.security.filters.JwtUtil;
import com.example.CinemaTicketAutomation.security.UserDetailsImpl;
import com.example.CinemaTicketAutomation.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AppUserRepository appUserRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse login(AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Authentication nesnesinden UserDetailsImpl'i alıyoruz
            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            
            String jwt = jwtUtil.generateToken(userDetails);
            
            String role = userDetails.getAuthorities().stream()
                    .findFirst()
                    .map(authority -> authority.getAuthority().replace("ROLE_", ""))
                    .orElse("");
            
            return AuthResponse.builder()
                    .token(jwt)
                    .username(userDetails.getUsername())
                    .role(role)
                    .build();
            
        } catch (AuthenticationException e) {
            throw new RuntimeException("Kimlik doğrulama başarısız: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Email kontrolü
        if (appUserRepository.existsByMail(request.email())) {
            throw new RuntimeException("Bu email zaten kullanımda: " + request.email());
        }
        
        // Kullanıcı adı kontrolü
        if (appUserRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Bu kullanıcı adı zaten kullanımda: " + request.username());
        }
        
        // Yeni kullanıcı oluştur
        AppUser appUser = AppUser.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .mail(request.email())
                .firstname(request.firstName())
                .lastName(request.lastName())
                .role(Role.ROLE_USER)
                .build();
        
        AppUser savedAppUser = appUserRepository.save(appUser);
        
        // UserDetailsImpl oluştur
        UserDetailsImpl userDetails = new UserDetailsImpl(savedAppUser);
        
        // Token oluştur
        String jwt = jwtUtil.generateToken(userDetails);
        
        return AuthResponse.builder()
                .token(jwt)
                .username(userDetails.getUsername())
                .role("USER")
                .build();
    }
} 