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
import org.springframework.web.bind.annotation.*;

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

    // Metoda obsługująca wyświetlanie listy produktów.
    @GetMapping("/productList")
    public String listProducts(
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        Pageable pageable = PageRequest.of(page, pageSize);
        Page<ProductDTO> productsPage = productService.findAllProductDTOs(pageable);

        // Dodaje atrybuty do modelu, aby móc je wyświetlić w widoku.
        model.addAttribute("productsPage", productsPage);
        model.addAttribute("pageSize", pageSize);

        return "products_list"; // Zwraca nazwę widoku listy produktów.
    }

    // Metoda wyświetlająca formularz wyszukiwania produktów.
    @GetMapping("/searchProduct/form")
    public String showSearchForm(Model model) {
        // Przygotowuje dane do formularza wyszukiwania.
        model.addAttribute("categories", categoryService.findAll());
        model.addAttribute("authors", authorService.findAll());
        return "product_search_form"; // Zwraca nazwę widoku formularza wyszukiwania.
    }

    // Metoda obsługująca wyszukiwanie produktów.
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

        // Dodaje atrybuty do modelu, aby móc je wyświetlić w wynikach wyszukiwania.
        model.addAttribute("productsPage", products);
        model.addAttribute("selectedPageSize", size);
        model.addAttribute("title", title);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("authorId", authorId);

        return "product_search_results"; // Zwraca nazwę widoku wyników wyszukiwania.
    }

    // Metoda obsługująca wyjątek nieznalezionego produktu.
    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFound(Model model) {
        model.addAttribute("error", "Produkt nie znaleziony");
        return "product_not_found"; // Zwraca nazwę widoku błędu.
    }

    // Metoda wyświetlająca szczegóły produktu.
    @GetMapping("/productDetails/{productId}")
    public String productDetails(@PathVariable("productId") Long productId, Model model) {
        ProductDTO productDTO = productService.findProductDTOById(productId);
        model.addAttribute("product", productDTO);
        return "product_details"; // Zwraca nazwę widoku szczegółów produktu.
    }

    // Metoda wyświetlająca drzewo kategorii.
    @GetMapping("/categoryTree")
    public String categoryTree() {
        return "categories_tree"; // Zwraca nazwę widoku drzewa kategorii.
    }

    // Metoda obsługująca wyświetlanie danych pogodowych.
    @GetMapping("/weather")
    public String getWeather(@RequestParam(name = "city", required = false) String city, Model model, Principal principal) {
        Optional<String> weatherData = weatherService.getWeatherData(city, principal);
        if (weatherData.isPresent()) {
            model.addAttribute("weatherData", weatherData.get());
            return "weather"; // nazwa widoku
        } else {
            model.addAttribute("error", "Nie można znaleźć danych o pogodzie.");
            return "weather";
        }
    }

    @PostMapping("/weather")
    public String handleWeatherForm(@RequestParam(name = "city") String city, Model model, Principal principal) {
        return "redirect:/weather?city=" + city; // Przekierowanie do metody GET, aby wyświetlić wyniki.
    }
}