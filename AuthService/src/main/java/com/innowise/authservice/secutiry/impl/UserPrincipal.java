package com.innowise.authservice.secutiry.impl;

import com.innowise.authservice.model.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * @ClassName UserPrincipal
 * @Description Adapter class that bridges the {@link User} entity with Spring Security's {@link UserDetails} interface.
 * @Author dshparko
 * @Date 07.10.2025 14:23
 * @Version 1.0
 */
public record UserPrincipal(User user) implements UserDetails {
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getRole().getName()));
    }

    @Override
    public String getPassword() {
        return user.getCredential().getPasswordHash();
    }

    @Override
    public String getUsername() {
        return user.getCredential().getEmail();
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
        return user.isActive();
    }

    public static UserPrincipal of(User user) {
        return new UserPrincipal(user);
    }

}
