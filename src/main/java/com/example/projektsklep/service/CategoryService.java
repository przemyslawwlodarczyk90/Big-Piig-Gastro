package com.example.projektsklep.service;

import com.example.projektsklep.model.dto.CategoryDTO;
import com.example.projektsklep.model.dto.CategoryTreeDTO;
import com.example.projektsklep.model.entities.product.Category;
import com.example.projektsklep.model.entities.product.CategoryTree;
import com.example.projektsklep.model.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;



@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    // Metoda do dodawania nowej kategorii w bazie danych.
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    // Metoda do dodawania nowej kategorii wraz z przypisaniem kategorii nadrzędnej.
    public CategoryDTO addCategoryWithParent(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());

        // Przypisanie kategorii nadrzędnej, jeśli została podana.
        if (categoryDTO.parentCategoryId() != null) {
            Category parentCategory = categoryRepository.findById(categoryDTO.parentCategoryId())
                    .orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParentCategory(parentCategory);
        }

        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryDTO(savedCategory);
    }

    // Pobieranie wszystkich kategorii jako DTO.
    public List<CategoryDTO> getAllCategoryDTOs() {
        return categoryRepository.findAll().stream()
                .map(this::convertToCategoryDTO)
                .collect(Collectors.toList());
    }

    // Pobieranie pojedynczej kategorii po ID jako DTO.
    public CategoryDTO getCategoryDTOById(Long id) {
        return categoryRepository.findById(id)
                .map(this::convertToCategoryDTO)
                .orElse(null);
    }

    // Pobieranie wszystkich kategorii z bazy danych.
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    // Aktualizacja danych kategorii.
    public CategoryDTO updateCategoryDTO(Long id, CategoryDTO updatedCategoryDTO) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        updateCategoryData(category, updatedCategoryDTO);
        return convertToCategoryDTO(categoryRepository.save(category));
    }

    // Generowanie drzewa kategorii.
    public List<CategoryTree> getCategoriesTree() {
        List<Category> categories = categoryRepository.findAll();
        Map<Long, CategoryTree> categoryTreesMap = new HashMap<>();

        // Inicjalizacja węzłów drzewa.
        for (Category category : categories) {
            CategoryTree categoryTree = new CategoryTree();
            categoryTree.setId(category.getId());
            categoryTree.setName(category.getName());
            categoryTree.setParent(category.getParent() != null ? new CategoryTree(category.getParent().getId(), category.getParent().getName(), null) : null);
            categoryTree.setChildren(new ArrayList<>());

            categoryTreesMap.put(category.getId(), categoryTree);
        }

        // Budowanie struktury drzewa.
        for (Category category : categories) {
            if (category.getParent() != null) {
                CategoryTree parentCategoryTree = categoryTreesMap.get(category.getParent().getId());
                parentCategoryTree.getChildren().add(categoryTreesMap.get(category.getId()));
            }
        }

        return new ArrayList<>(categoryTreesMap.values());
    }

    // Zapis lub aktualizacja kategorii.
    public CategoryDTO saveOrUpdateCategory(CategoryDTO categoryDTO) {
        Category category = new Category();
        category.setName(categoryDTO.name());
        if (categoryDTO.parentCategoryId() != null) {
            Category parent = categoryRepository.findById(categoryDTO.parentCategoryId()).orElseThrow(() -> new RuntimeException("Parent category not found"));
            category.setParentCategory(parent);
        }
        Category savedCategory = categoryRepository.save(category);
        return convertToCategoryDTO(savedCategory);
    }

    // Usuwanie kategorii po ID z bazy danych.
    @Transactional
    public void deleteCategoryById(Long id) {
        categoryRepository.deleteById(id);
    }

    // Konwersja encji kategorii na DTO.
    private CategoryDTO convertToCategoryDTO(Category category) {
        String parentCategoryName = category.getParentCategory() != null ? category.getParentCategory().getName() : null;
        return new CategoryDTO(
                category.getId(),
                category.getName(),
                category.getParentCategory() != null ? category.getParentCategory().getId() : null,
                parentCategoryName
        );
    }

    // Aktualizacja danych kategorii na podstawie DTO.
    private void updateCategoryData(Category category, CategoryDTO categoryDTO) {
        category.setName(categoryDTO.name());
        // Tutaj można dodać logikę aktualizacji kategorii nadrzędnej, jeśli jest wymagane.
    }

    // Pobieranie kategorii z nazwą kategorii nadrzędnej.
    public List<CategoryDTO> getCategoriesWithParentName() {
        List<CategoryDTO> categories = getAllCategoryDTOs().stream().map(category -> {
            String parentCategoryName = category.parentCategoryId() != null
                    ? getCategoryDTOById(category.parentCategoryId()).name()
                    : null;
            return new CategoryDTO(category.id(), category.name(), category.parentCategoryId(), parentCategoryName);
        }).collect(Collectors.toList());
        return categories;
    }

    // Przygotowanie modelu dla formularza dodawania kategorii.
    public Model prepareAddCategoryModel(Model model) {
        if (!model.containsAttribute("categoryDTO")) {
            model.addAttribute("categoryDTO", new CategoryDTO(null, "", null, null));
        }

        List<CategoryDTO> allCategories = getAllCategoryDTOs();
        model.addAttribute("allCategories", allCategories);
        return model;
    }
}