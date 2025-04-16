package com.example.CinemaTicketAutomation.security;

import com.example.CinemaTicketAutomation.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.findByUsername(username)
                .map(UserDetailsImpl::new) // Optional<User> → Optional<UserDetailsImpl>
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find user with the username: " + username));
    }
} 