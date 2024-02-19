package com.example.projektsklep.utils;

import com.example.projektsklep.model.dto.AddressDTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
@Data
@Builder
public class RegistrationForm {
    private Long id;
    @NotBlank String firstName;
    @NotBlank String lastName;
    @NotBlank String email;
    String password;
    AddressDTO address;
    String role;
}