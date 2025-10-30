package com.innowise.authservice.service.impl;


import com.innowise.authservice.exception.ResourceNotFoundException;
import com.innowise.authservice.model.dto.UserDto;
import com.innowise.authservice.service.UserClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @ClassName UserClient
 * @Description Implementation o
 * @Author dshparko
 * @Date 13.10.2025 21:19
 * @Version 1.0
 */
@Service
public class UserClientImpl implements UserClient {

    private final RestTemplate restTemplate;

    @Value("${user-service.url}")
    private String userServiceUrl;

    @Value("${user-service.path}")
    private String userApiPath;

    public UserClientImpl(
            @Value("${user-service.url}") String userServiceUrl,
            @Value("${user-service.path}") String userApiPath,
            RestTemplate restTemplate) {
        this.userServiceUrl = userServiceUrl;
        this.userApiPath = userApiPath;
        this.restTemplate = restTemplate;
    }

    @Override
    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackCreateUser")
    public String createUser(UserDto userDto, String token) {
        HttpEntity<UserDto> requestEntity = buildRequestEntity(userDto, token);
        String endpoint = userServiceUrl + userApiPath;


        ResponseEntity<UserDto> response = restTemplate.postForEntity(
                endpoint, requestEntity, UserDto.class
        );

        return extractUserEmail(response);
    }

    private HttpEntity<UserDto> buildRequestEntity(UserDto userDto, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(userDto, headers);
    }

    private String extractUserEmail(ResponseEntity<UserDto> response) {
        HttpStatusCode status = response.getStatusCode();
        UserDto body = response.getBody();

        if (status.is2xxSuccessful() && body != null && body.getEmail() != null && !body.getEmail().isBlank()) {
            return body.getEmail();
        }

        throw new ResourceNotFoundException("User ID not returned from UserService. Status: " + status);
    }

    private String fallbackCreateUser(UserDto userDto, Throwable ex) {
        throw new ResourceNotFoundException("User service is unavailable. Reason: " + ex.getMessage());
    }
}
