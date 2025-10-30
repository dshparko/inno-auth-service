package com.innowise.authservice.secutiry.impl;

import com.innowise.authservice.model.RoleEnum;
import com.innowise.authservice.secutiry.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static com.innowise.authservice.secutiry.AuthConstant.ACCESS_TYPE;
import static com.innowise.authservice.secutiry.AuthConstant.REFRESH_TYPE;

/**
 * @ClassName JwtService
 * @Description Service responsible for JWT token generation, validation, and claim extraction.
 * @Author dshparko
 * @Date 04.10.2025 19:24
 * @Version 1.0
 */
@Service
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.access-token.expiration}")
    private long accessTokenExpirationMs;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshTokenExpirationMs;

    public String generateAccessToken(UserDetails user) {
        return buildToken(user, accessTokenExpirationMs, ACCESS_TYPE);
    }

    public String generateRefreshToken(UserDetails user) {
        return buildToken(user, refreshTokenExpirationMs, REFRESH_TYPE);
    }

    private String buildToken(UserDetails user, long expirationMs, String tokenType) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);
        Map<String, Object> claims = buildClaims(user, tokenType);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(user.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isRefreshToken(String token) {
        return REFRESH_TYPE.equals(extractType(token));
    }

    private Map<String, Object> buildClaims(UserDetails user, String tokenType) {
        Map<String, Object> claims = new HashMap<>();
        String role = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(RoleEnum.USER.name());

        claims.put("role", role);
        claims.put("type", tokenType);
        return claims;
    }


    public String extractEmail(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public String extractType(String token) {
        return extractAllClaims(token).get("type", String.class);
    }

    public boolean isTokenValid(String token, String expectedEmail, String expectedRole) {
        String extractedEmail = extractEmail(token);
        String extractedRole = extractRole(token);

        return extractedEmail != null
                && extractedRole != null
                && extractedEmail.equals(expectedEmail)
                && extractedRole.equals(expectedRole)
                && !isTokenExpired(token);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = extractAllClaims(token);
        return resolver.apply(claims);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role", String.class);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

