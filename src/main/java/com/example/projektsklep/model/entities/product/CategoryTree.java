package com.example.projektsklep.model.entities.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryTree {

    private Long id;

    private String name;

    private CategoryTree parent;

    private List<CategoryTree> children;


    public CategoryTree(Long id, String name, Long aLong) {
    }
}