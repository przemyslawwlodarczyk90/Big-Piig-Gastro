package com.example.projektsklep.model.dto;

import lombok.Builder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Builder
public record ProducerDTO(
        Long id,
        @NotBlank
        @Size(min = 2, max = 50, message = "Nazwa musi zawierać od 2 do 50 znaków")
        String name
) {}