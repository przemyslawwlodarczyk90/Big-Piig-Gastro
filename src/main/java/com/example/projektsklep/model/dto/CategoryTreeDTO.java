package com.example.projektsklep.model.dto;

import com.example.projektsklep.model.entities.product.Category;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Data
@Builder
public class CategoryTreeDTO {

    private Long id;

    private String name;

    private List<CategoryTreeDTO> children;

    public CategoryTreeDTO() {
    }

    public CategoryTreeDTO(Long id, String name, List<CategoryTreeDTO> children) {
        this.id = id;
        this.name = name;
        this.children = children;
    }

    public CategoryTreeDTO(Category category) {
        this.id = category.getId();
        this.name = category.getName();

        List<CategoryTreeDTO> childDTOs = new ArrayList<>();
        for (Category child : category.getChildren()) {
            childDTOs.add(new CategoryTreeDTO(child));
        }

        this.children = childDTOs;
    }

}

