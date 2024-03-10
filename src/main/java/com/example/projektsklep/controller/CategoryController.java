package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.CategoryDTO;
import com.example.projektsklep.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;


    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    @Operation(summary = "Wyświetla listę kategorii")
    @GetMapping
    public String listCategories(Model model) {
        List<CategoryDTO> categories = categoryService.getCategoriesWithParentName();
        model.addAttribute("categories", categories);
        return "admin_category_list";
    }

}