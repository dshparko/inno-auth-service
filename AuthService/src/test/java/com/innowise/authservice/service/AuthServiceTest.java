package com.innowise.authservice.service;

import com.innowise.authservice.exception.InvalidResourceException;
import com.innowise.authservice.exception.ResourceNotFoundException;
import com.innowise.authservice.model.RoleEnum;
import com.innowise.authservice.model.dto.AuthenticationResponse;
import com.innowise.authservice.model.dto.LoginDto;
import com.innowise.authservice.model.dto.TokenInfo;
import com.innowise.authservice.model.dto.TokenPayload;
import com.innowise.authservice.model.entity.Credential;
import com.innowise.authservice.model.entity.User;
import com.innowise.authservice.repository.CredentialRepository;
import com.innowise.authservice.repository.RoleRepository;
import com.innowise.authservice.secutiry.JwtService;
import com.innowise.authservice.secutiry.PasswordEncoder;
import com.innowise.authservice.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    JwtService jwtService;
    @Mock
    UserService userService;
    @Mock
    RoleRepository roleRepository;
    @Mock
    CredentialRepository credentialRepository;
    @Mock
    PasswordEncoder encoder;

    @InjectMocks
    AuthServiceImpl authService;

    @Test
    void login_shouldReturnTokens_whenCredentialsAreValid() {
        LoginDto request = new LoginDto("user@example.com", "password");
        Credential credential = new Credential();
        credential.setEmail("user@example.com");
        credential.setSalt("salt");
        credential.setPasswordHash("hashed");
        var user = new User();
        credential.setUser(user);

        when(credentialRepository.findByEmail("user@example.com")).thenReturn(Optional.of(credential));
        when(encoder.matches("password", "salt", "hashed")).thenReturn(true);
        when(jwtService.generateAccessToken(any())).thenReturn("access-token");
        when(jwtService.generateRefreshToken(any())).thenReturn("refresh-token");

        AuthenticationResponse response = authService.login(request);

        assertEquals("access-token", response.accessToken());
        assertEquals("refresh-token", response.refreshToken());
    }

    @Test
    void login_shouldThrow_whenPasswordIsInvalid() {
        LoginDto request = new LoginDto("user@example.com", "wrong");
        Credential credential = new Credential();
        credential.setSalt("salt");
        credential.setPasswordHash("hashed");

        when(credentialRepository.findByEmail("user@example.com")).thenReturn(Optional.of(credential));
        when(encoder.matches("wrong", "salt", "hashed")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> authService.login(request));
    }

    @Test
    void validate_shouldReturnTokenInfo_whenTokenIsValid() {
        TokenPayload payload = new TokenPayload("valid-token");

        when(jwtService.isTokenExpired("valid-token")).thenReturn(false);
        when(jwtService.extractEmail("valid-token")).thenReturn("user@example.com");
        when(jwtService.extractRole("valid-token")).thenReturn(RoleEnum.USER.name());
        when(jwtService.isTokenValid("valid-token", "user@example.com", RoleEnum.USER.name())).thenReturn(true);

        TokenInfo info = authService.validate(payload);

        assertEquals("user@example.com", info.email());
        assertEquals(RoleEnum.USER.name(), info.role());
    }

    @Test
    void refresh_shouldReturnNewTokens_whenTokenIsValid() {
        TokenPayload payload = new TokenPayload("refresh-token");

        when(jwtService.extractEmail("refresh-token")).thenReturn("user@example.com");
        when(jwtService.extractRole("refresh-token")).thenReturn(RoleEnum.USER.name());
        when(jwtService.isTokenValid("refresh-token", "user@example.com", RoleEnum.USER.name())).thenReturn(true);

        User user = new User();
        when(userService.findByEmail("user@example.com")).thenReturn(user);
        when(jwtService.generateAccessToken(any())).thenReturn("new-access");
        when(jwtService.generateRefreshToken(any())).thenReturn("new-refresh");

        AuthenticationResponse response = authService.refresh(payload);

        assertEquals("new-access", response.accessToken());
        assertEquals("new-refresh", response.refreshToken());
    }

    @Test
    void validate_shouldThrow_whenTokenExpired() {
        TokenPayload payload = new TokenPayload("expired-token");

        when(jwtService.isTokenExpired("expired-token")).thenReturn(true);

        assertThrows(InvalidResourceException.class, () -> authService.validate(payload));
    }

    @Test
    void refresh_shouldThrow_whenTokenIsInvalid() {
        TokenPayload payload = new TokenPayload("invalid-token");

        when(jwtService.extractEmail("invalid-token")).thenReturn("user@example.com");
        when(jwtService.extractRole("invalid-token")).thenReturn(RoleEnum.USER.name());
        when(jwtService.isTokenValid("invalid-token", "user@example.com", RoleEnum.USER.name())).thenReturn(false);

        assertThrows(InvalidResourceException.class, () -> authService.refresh(payload));
    }
}