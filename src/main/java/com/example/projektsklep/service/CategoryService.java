package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.CategoryDTO;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }



    public CategoryDTO addCategoryWithParent(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());

        if (categoryDTO.parentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.parentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParentCategory(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryDTO(savedCategory);
    }

    public List<CategoryDTO> getAllCategoryDTOs() {
        return categoryRepository.findAll().stream()
                .map(this::convertToCategoryDTO)
                .collect(Collectors.toList());
    }

    public CategoryDTO getCategoryDTOById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToCategoryDTO)
                .orElse(null);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public void updateCategoryDTO(Long id, CategoryDTO updatedCategoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updateCategoryData(category, updatedCategoryDTO);
        convertToCategoryDTO(categoryRepository.save(category));
    }



    @Transactional
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryDTO convertToCategoryDTO(Category category) {
        String parentCategoryName = category.getParentCategory() != null ? category.getParentCategory().getName() : null;
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getParentCategory() != null ? category.getParentCategory().getId() : null,
                parentCategoryName
        );
    }

    private void updateCategoryData(Category category, CategoryDTO categoryDTO) {
        category.setName(categoryDTO.name());
    }

    public List<CategoryDTO> getCategoriesWithParentName() {
        return getAllCategoryDTOs().stream().map(category -> {
            String parentCategoryName = category.parentCategoryId() != null
                    ? getCategoryDTOById(category.parentCategoryId()).name()
                    : null;
            return new CategoryDTO(category.id(), category.name(), category.parentCategoryId(), parentCategoryName);
        }).collect(Collectors.toList());
    }
    public void prepareAddCategoryModel(Model model) {
        if (!model.containsAttribute("categoryDTO")) {
            model.addAttribute("categoryDTO", new CategoryDTO(null, "", null, null));
        }

        List<CategoryDTO> allCategories = getAllCategoryDTOs();
        model.addAttribute("allCategories", allCategories);
    }
}