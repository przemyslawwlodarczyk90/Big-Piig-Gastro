package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.CategoryDTO;
import com.example.projektsklep.service.CategoryService;
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

    /**
     * Wyświetla listę wszystkich kategorii w panelu administracyjnym.
     *
     * Metoda obsługuje żądanie GET pod adresem "/categories". Pobiera listę wszystkich kategorii
     * za pomocą serwisu categoryService, a następnie przekazuje ją do modelu, aby była dostępna
     * w widoku Thymeleaf.
     *
     * @param model Model przekazywany do widoku, służący do przekazywania danych między kontrolerem a widokiem.
     * @return Nazwa widoku Thymeleaf (plik HTML), który ma zostać wyrenderowany.
     */
    @GetMapping
    public String listCategories(Model model) {
        // Pobiera listę kategorii wraz z nazwami ich kategorii nadrzędnych.
        List<CategoryDTO> categories = categoryService.getCategoriesWithParentName();
        // Dodaje listę kategorii do modelu, aby była dostępna w widoku.
        model.addAttribute("categories", categories);
        // Zwraca nazwę widoku Thymeleaf, który ma zostać wyświetlony.
        return "admin_category_list";
    }

}