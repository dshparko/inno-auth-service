package com.innowise.authservice.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
public class UserDto {

    private Long id;

    private String name;

    private String surname;

    private String email;

    private LocalDate birthDate;

    private List<CardDto> cards;

}
