package com.example.projektsklep.model.dto;


import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@Builder
public record UserDTO(
        Long id,
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email,
        String password,
        AddressDTO address,
        Set<RoleDTO> roles

){}