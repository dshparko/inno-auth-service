package com.innowise.authservice.secutiry;

import com.innowise.authservice.exception.AuthServiceException;
import com.innowise.authservice.model.entity.User;
import com.innowise.authservice.secutiry.impl.UserPrincipal;
import com.innowise.authservice.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.innowise.authservice.secutiry.AuthConstant.AUTH_HEADER;
import static com.innowise.authservice.secutiry.AuthConstant.TOKEN_PREFIX;

/**
 * @ClassName JwtFilter
 * @Description Security filter responsible for extracting and validating JWT tokens from incoming requests.
 * @Author dshparko
 * @Date 04.10.2025 19:35
 * @Version 1.0
 */
@AllArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTH_HEADER);

        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = authHeader.substring(TOKEN_PREFIX.length());

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            User user = resolveUserFromToken(token);
            UserPrincipal principal = UserPrincipal.of(user);

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private User resolveUserFromToken(String token) {
        final String id = jwtService.extractUserId(token);

        if (id == null || id.isBlank()) {
            throw new AuthServiceException("Missing user ID in token");
        }

        try {
            Long userId = Long.valueOf(id);
            return userService.findById(userId);
        } catch (NumberFormatException e) {
            throw new AuthServiceException("Invalid user ID format in token");
        }
    }

}

