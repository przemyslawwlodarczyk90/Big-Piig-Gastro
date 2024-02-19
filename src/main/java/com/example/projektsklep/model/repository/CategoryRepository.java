package com.example.projektsklep.model.repository;


import com.example.projektsklep.model.entities.product.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {


    List<Category> findByParentCategoryId(Long parentCategoryId);

    Category save(Category category);

    List<Category> findAll();

    Optional<Category> findById(Long id);
}


