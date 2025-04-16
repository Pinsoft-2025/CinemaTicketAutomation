package com.example.CinemaTicketAutomation.security;

import com.example.CinemaTicketAutomation.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final AppUserService appUserService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return appUserService.findByUsername(username)
                .map(UserDetailsImpl::new) // Optional<User> → Optional<UserDetailsImpl>
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find user with the username: " + username));
    }
} 