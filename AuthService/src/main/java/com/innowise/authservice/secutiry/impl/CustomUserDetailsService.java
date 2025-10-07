package com.innowise.authservice.secutiry.impl;

import com.innowise.authservice.model.entity.User;
import com.innowise.authservice.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @ClassName CustomUserDetailsService
 * @Description Custom implementation of {@link org.springframework.security.core.userdetails.UserDetailsService}.
 * @Author dshparko
 * @Date 06.10.2025 22:02
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final CredentialRepository credentialRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = credentialRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found")).getUser();
        return UserPrincipal.of(user);
    }
}

