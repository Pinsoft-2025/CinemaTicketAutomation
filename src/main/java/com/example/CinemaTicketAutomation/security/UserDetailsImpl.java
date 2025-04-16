package com.example.CinemaTicketAutomation.security;

import com.example.CinemaTicketAutomation.entity.AppUser;
import com.example.CinemaTicketAutomation.entity.enums.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
public class UserDetailsImpl implements UserDetails {

    private final AppUser user;

    public UserDetailsImpl(AppUser user) {
        this.user = user;
    }

    public static UserDetailsImpl build(AppUser user) {
        return new UserDetailsImpl(user);
    }

    public Long getId() {
        return user.getId();
    }

    public String getEmail() {
        return user.getMail();
    }

    public String getFirstName() {
        return user.getFirstname();
    }

    public String getLastName() {
        return user.getLastName();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(user.getRole());
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}