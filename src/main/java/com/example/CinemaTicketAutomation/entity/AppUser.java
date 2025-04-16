package com.example.CinemaTicketAutomation.entity;

import com.example.CinemaTicketAutomation.entity.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@Entity
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "mail", unique = true, nullable = false)
    private String mail;

    @Column(name = "phone_number", unique = true)
    private String phoneNumber;

    @Column(name = "firstname")
    private String firstname = "John";

    @Column(name = "lastname")
    private String lastName = "Doe";

    @Column(name = "age")
    private int age;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;


    @OneToMany(mappedBy = "appUser")
    private List<Reservation> reservations;
}
