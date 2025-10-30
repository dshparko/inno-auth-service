package com.innowise.authservice.model.dto;

public record TokenInfo(
        String email,
        String role,
        String type
) {
}

