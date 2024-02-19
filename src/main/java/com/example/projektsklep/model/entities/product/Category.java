package com.example.projektsklep.model.entities.product;

import com.example.projektsklep.model.dto.CategoryTreeDTO;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Entity

public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory")
    private List<Category> children;

    public Category() {
        this.children = new ArrayList<>();
    }

    public Category(String name) {
        this.name = name;
    }

    public List<Category> getChildren() {
        return children;
    }

    public Category getParent() {
        return parentCategory;
    }

    public static CategoryTreeDTO toTreeDTO(Category category) {
        CategoryTreeDTO categoryTreeDTO = new CategoryTreeDTO();
        categoryTreeDTO.setId(category.getId());
        categoryTreeDTO.setName(category.getName());
        return categoryTreeDTO;
    }

    public void addChild(Category childCategory) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(childCategory);
        childCategory.setParentCategory(this);
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return name.equals(category.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}