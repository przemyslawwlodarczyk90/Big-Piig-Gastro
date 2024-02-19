package com.example.projektsklep.model.dto;

import lombok.Builder;
import javax.validation.constraints.NotBlank;

@Builder

public record AddressDTO(
        Long id,
        @NotBlank
        String street,
        @NotBlank
        String city,
        @NotBlank
        String postalCode,
        @NotBlank
        String country
) {
}