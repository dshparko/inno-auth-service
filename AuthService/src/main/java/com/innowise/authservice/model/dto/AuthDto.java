package com.innowise.authservice.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * @ClassName AuthDto
 * @Description Data Transfer Object representing user registration or creation request.
 * @Author dshparko
 * @Date 05.10.2025 16:33
 * @Version 1.0
 */
public record AuthDto(

        @NotBlank(message = "Email must not be blank")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password must not be blank")
        @Size(min = 6, max = 64, message = "Password must be between 6 and 64 characters")
        String password,

        @NotBlank(message = "Role must not be blank")
        String role
) {
}
