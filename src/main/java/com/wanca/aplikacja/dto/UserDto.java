package com.wanca.aplikacja.dto;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class UserDto {
    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Size(max = 50)
    private String surname;

    @Size(min = 5, max = 32)
    private String password;

    @Email
    @Size(min = 5, max = 32)
    private String email;
}