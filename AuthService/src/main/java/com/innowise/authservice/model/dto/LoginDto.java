package com.innowise.authservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @ClassName LoginDto
 * @Description Data Transfer Object representing login credentials.
 * @Author dshparko
 * @Date 05.10.2025 13:00
 * @Version 1.0
 */

public record LoginDto(
        @NotBlank(message = "Username must be filled")
        @Size(min = 4, max = 60, message = "The name should be 4 to 60 characters")
        @Email
        String email,

        @NotBlank(message = "Password must not be empty")
        @Size(min = 6, message = "Password must be at least 6 characters")
        String password
) {
}
