package com.innowise.authservice.controller;

import com.innowise.authservice.model.dto.AuthDto;
import com.innowise.authservice.model.dto.AuthenticationResponse;
import com.innowise.authservice.model.dto.LoginDto;
import com.innowise.authservice.model.dto.TokenPayload;
import com.innowise.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName AuthController
 * @Description Provides endpoints for authentication operations.
 * @Author dshparko
 * @Date 04.10.2025 18:35
 * @Version 1.0
 */

@AllArgsConstructor
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid AuthDto request,
                                         @RequestHeader(value = "Authorization", required = false) String authHeader) {
        authService.register(request, authHeader);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/validate")
    public ResponseEntity<String> validate(@RequestBody @Valid TokenPayload token) {
        return ResponseEntity.ok(authService.validate(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponse> refresh(@RequestBody @Valid TokenPayload request) {
        return ResponseEntity.ok(authService.refresh(request));
    }

}
