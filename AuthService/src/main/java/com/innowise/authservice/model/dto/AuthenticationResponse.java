package com.innowise.authservice.model.dto;

public record AuthenticationResponse(
        String accessToken,
        String refreshToken
) {
}
