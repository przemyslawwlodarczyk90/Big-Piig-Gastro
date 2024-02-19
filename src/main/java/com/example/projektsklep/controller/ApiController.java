package com.example.projektsklep.controller;

import com.example.projektsklep.exception.ProductNotFoundException;
import com.example.projektsklep.model.dto.ProductDTO;
import com.example.projektsklep.service.ProducerService;
import com.example.projektsklep.service.CategoryService;
import com.example.projektsklep.service.ProductService;
import com.example.projektsklep.service.WeatherService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Optional;

@Controller
@RequestMapping("/api")
public class ApiController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final ProducerService authorService;
    private final WeatherService weatherService;

    public ApiController(ProductService productService, CategoryService categoryService, ProducerService authorService, WeatherService weatherService) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.authorService = authorService;
        this.weatherService = weatherService;
    }

    @GetMapping("/productList")
    public String listProducts(
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductDTO> productsPage = productService.findAllProductDTOs(pageable);

        model.addAttribute("productsPage", productsPage);
        model.addAttribute("pageSize", pageSize);


        return "products_list";
    }

    @GetMapping("/searchProduct/form")
    public String showSearchForm(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "product_search_form";
    }

    @GetMapping("/searchProduct")
    public String searchProducts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDTO> products = productService.searchProducts(title, categoryId, authorId, pageable);

        model.addAttribute("productsPage", products);
        model.addAttribute("selectedPageSize", size);
        model.addAttribute("title", title);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("authorId", authorId);

        return "product_search_results";
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(Model model) {
        model.addAttribute("error", "Produkt nie znaleziony");
        return "product_not_found"; // Nazwa widoku błędu
    }

    @GetMapping("/productDetails")
    public String productDetails(@RequestParam("productId") Long productId, Model model) {
        ProductDTO productDTO = productService.findProductDTOById(productId);
        model.addAttribute("product", productDTO);
        return "product_details";
    }

    @GetMapping("/categoryTree")
    public String categoryTree() {
        return "categories_tree"; // Zwraca userPanel.html
    }


    @GetMapping("/weather")
    public String getWeather(@RequestParam(name = "city", required = false) String city, Model model, Principal principal) {
        Optional<String> weatherData = weatherService.getWeatherData(city, principal);
        if (weatherData.isPresent()) {
            model.addAttribute("weatherData", weatherData.get());
            return "weather";
        } else {
            model.addAttribute("error", "Nie można znaleźć danych o pogodzie.");
            return "weather";
        }
    }
}
