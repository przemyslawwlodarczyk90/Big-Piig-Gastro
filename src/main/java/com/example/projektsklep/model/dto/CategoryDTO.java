package com.example.projektsklep.model.dto;

import lombok.Builder;


@Builder
public record CategoryDTO(
        Long id,
        String name,
        Long parentCategoryId,
        String parentCategoryName
) { }