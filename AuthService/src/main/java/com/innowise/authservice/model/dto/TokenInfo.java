package com.innowise.authservice.model.dto;

public record TokenInfo(
        String id,
        String role,
        String type
) {
}

