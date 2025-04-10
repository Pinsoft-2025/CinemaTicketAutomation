package com.example.CinemaTicketAutomation.repository;

import com.example.CinemaTicketAutomation.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
