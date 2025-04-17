package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.AppUser;
import com.example.CinemaTicketAutomation.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    boolean existsByMail(String email);
    boolean existsByUsername(String username);
    Optional<AppUser> findByUsername(String username);
    Optional<AppUser> findByMail(String email);
    List<AppUser> findByRole(Role role);
    List<AppUser> findByUsernameContainingIgnoreCase(String username);
    List<AppUser> findByMailContainingIgnoreCase(String email);
    long countByRole(Role role);
}
