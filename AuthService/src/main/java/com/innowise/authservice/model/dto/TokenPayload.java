package com.innowise.authservice.model.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * @ClassName TokenPayload
 * @Description Data Transfer Object representing a JWT token payload.
 * @Author dshparko
 * @Date 05.10.2025 16:33
 * @Version 1.0
 */
public record TokenPayload(
        @NotBlank(message = "Token must not be empty")
        String token
) {
}