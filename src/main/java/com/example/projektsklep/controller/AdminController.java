package com.example.projektsklep.controller;


import com.example.projektsklep.exception.CategoryException;
import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.model.dto.*;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.model.repository.UserRepository;
import com.example.projektsklep.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

// Klasa kontrolera dla działań administracyjnych.
// Zapewnia metody dla różnych akcji, takich jak wyświetlanie panelu administratora, zarządzanie użytkownikami, produktami, kategoriami itp.
@Controller
@RequestMapping("/admin")
public class AdminController {

    // Wstrzyknięcie zależności do różnych serwisów używanych w kontrolerze.
    private final UserService userService;
    private final OrderService orderService;
    private final ProducerService authorService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final UserRepository userRepository;

    // Konstruktor z wstrzykniętymi zależnościami.
    public AdminController(UserService userService, OrderService orderService, ProducerService authorService, CategoryService categoryService, ProductService productService, UserRepository userRepository) {
        this.userService = userService;
        this.orderService = orderService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    // Metoda wyświetlająca główny panel administracyjny.
    @GetMapping("/panel")
    public String showAdminPanel() {
        return "adminPanel"; // Zwraca nazwę widoku panelu administracyjnego.
    }

    // Metoda wyświetlająca formularz wyszukiwania użytkowników.
    @GetMapping("/user_search")
    public String showUserSearchForm() {
        return "admin_user_search"; // Zwraca nazwę widoku formularza wyszukiwania użytkowników.
    }

    // Metoda obsługująca wyszukiwanie użytkowników po nazwisku.
    @PostMapping("/user_search")
    public String showUserOrders(@RequestParam String lastName, Model model) {
        List<UserDTO> users = userService.findUsersByLastName(lastName);
        model.addAttribute("users", users); // Dodaje wyniki wyszukiwania do modelu.
        return "admin_user_list"; // Zwraca nazwę widoku z listą znalezionych użytkowników.
    }

    // Metoda wyświetlająca szczegóły użytkownika o określonym ID.
    @GetMapping("/user_details/{userId}")
    public String userDetails(@PathVariable("userId") Long userId, Model model) {
        Optional<UserDTO> userOptional = userService.findUserById(userId);
        userOptional.ifPresent(user -> model.addAttribute("user", user));
        return userOptional.isPresent() ? "user_details" : "redirect:/admin/user_search"; // Zwraca widok szczegółów użytkownika lub przekierowuje do wyszukiwania.
    }

    // Metoda wyświetlająca formularz dodawania nowego producenta.
    @GetMapping("/author")
    public String showAuthorForm(Model model) {
        model.addAttribute("author", new ProducerDTO(null, ""));
        return "admin_producer_form"; // Zwraca nazwę widoku formularza dodawania producenta.
    }

    // Metoda obsługująca zapis nowego producenta do bazy danych.
    @PostMapping("/author")
    public String saveAuthor(@Valid @ModelAttribute("author") ProducerDTO authorDTO, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            authorService.saveAuthor(authorDTO); // Zapisuje producenta.
            return "redirect:/admin/authors"; // Przekierowuje do listy producentów.
        } else {
            return "admin_producer_form"; // W przypadku błędów zwraca formularz.
        }
    }

    // Metoda wyświetlająca stronę z listą producentów.
    @GetMapping("/authors")
    public String listAuthors(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProducerDTO> authorPage = authorService.findAllAuthorsPageable(pageable);
        model.addAttribute("authorPage", authorPage); // Dodaje stronę z producentami do modelu.
        return "admin_producers_list"; // Zwraca nazwę widoku z listą producentów.
    }

    // Metoda wyświetlająca formularz dodawania nowego produktu.
    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        productService.prepareAddProductFormModel(model); // Przygotowuje model dla formularza.
        return "admin_add_product"; // Zwraca nazwę widoku formularza dodawania produktu.
    }

    // Metoda obsługująca zapis nowego produktu do bazy danych.
    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute ProductDTO productDTO, Model model) {
        productService.saveProductDTO(productDTO); // Zapisuje produkt.
        productService.prepareAddProductFormModel(model); // Ponownie przygotowuje model.
        model.addAttribute("successMessage", "Produkt został dodany."); // Dodaje komunikat o sukcesie.
        return "admin_add_product"; // Zwraca nazwę widoku formularza z komunikatem o sukcesie.
    }

    // Metoda wyświetlająca zamówienia użytkownika o określonym ID.
    @GetMapping("/user_orders/{userId}")
    public String listUserOrders(@PathVariable Long userId, Model model) {
        List<OrderDTO> orders = orderService.findAllOrdersByUserId(userId);
        model.addAttribute("orders", orders); // Dodaje listę zamówień do modelu.
        return "admin_user_orders"; // Zwraca nazwę widoku z listą zamówień użytkownika.
    }

    // Metoda wyświetlająca zamówienia według statusu.
    @GetMapping("/ordersByStatus")
    public String getOrdersByStatus(@RequestParam(required = false) OrderStatus orderStatus, Model model) {
        orderService.prepareOrdersByStatusModel(orderStatus, model); // Przygotowuje model z zamówieniami o danym statusie.
        return "orders_by_status"; // Zwraca nazwę widoku z listą zamówień według statusu.
    }

    // Metoda wyświetlająca formularz do dodawania nowej kategorii.
    @GetMapping("/addCategory")
    public String showAddForm(Model model) {
        // Przygotowuje model dla formularza dodawania kategorii, w tym dostępne kategorie nadrzędne.
        categoryService.prepareAddCategoryModel(model);
        return "category_add"; // Zwraca nazwę widoku formularza dodawania kategorii.
    }

    // Metoda obsługująca dodawanie nowej kategorii do bazy danych.
    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute CategoryDTO categoryDTO) {
        // Próbuje dodać kategorię i przekierowuje do listy kategorii lub zgłasza wyjątek w przypadku błędu.
        try {
            categoryService.addCategoryWithParent(categoryDTO);
            return "redirect:/categories"; // Przekierowanie do listy kategorii.
        } catch (Exception e) {
            throw new CategoryException("Error adding category", e); // Rzucenie wyjątku w przypadku błędu.
        }
    }

    // Metoda wyświetlająca formularz edycji kategorii o określonym ID.
    @GetMapping("/editCategory/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        // Pobiera DTO kategorii po ID i dodaje do modelu.
        CategoryDTO categoryDTO = categoryService.getCategoryDTOById(id);
        model.addAttribute("category", categoryDTO);
        return "category_edit"; // Zwraca nazwę widoku formularza edycji kategorii.
    }

    // Metoda obsługująca aktualizację kategorii w bazie danych.
    @PostMapping("/editCategory/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute("category") CategoryDTO categoryDTO) {
        // Aktualizuje kategorię i przekierowuje do listy kategorii.
        categoryService.updateCategoryDTO(id, categoryDTO);
        return "redirect:/categories"; // Przekierowanie do listy kategorii.
    }

    // Metoda obsługująca usuwanie kategorii o określonym ID.
    @PostMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Long id) {
        // Usuwa kategorię i przekierowuje do listy kategorii.
        categoryService.deleteCategoryById(id);
        return "redirect:/categories"; // Przekierowanie do listy kategorii.
    }

    // Metoda wyświetlająca formularz edycji statusu zamówienia.
    @GetMapping("/editOrderStatus/{orderId}")
    public String showEditOrderForm(@PathVariable Long orderId, Model model) {
        // Próbuje przygotować model dla formularza edycji zamówienia.
        try {
            orderService.prepareEditOrderFormModel(orderId, model);
            return "order_edit_form"; // Zwraca nazwę widoku formularza edycji statusu zamówienia.
        } catch (OrderNotFoundException e) {
            model.addAttribute("error", "Zamówienie nie znalezione"); // Dodaje komunikat o błędzie do modelu.
            return "error"; // Zwraca widok strony błędu.
        }
    }

    // Metoda obsługująca aktualizację statusu zamówienia.
    @PostMapping("/editOrderStatus/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @ModelAttribute("order") OrderDTO orderDTO, Model model, HttpServletRequest request) {
        // Próbuje zaktualizować status zamówienia i przekierowuje do poprzedniej strony lub wyświetla błąd.
        try {
            orderService.updateOrderStatus(orderId, orderDTO.orderStatus());
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/user_orders"); // Przekierowanie do poprzedniej strony.
        } catch (OrderNotFoundException e) {
            model.addAttribute("error", "Zamówienie nie znalezione"); // Dodaje komunikat o błędzie do modelu.
            return "error"; // Zwraca widok strony błędu.
        } catch (Exception e) {
            model.addAttribute("error", "Błąd podczas aktualizacji statusu zamówienia"); // Dodaje komunikat o błędzie do modelu.
            return "order_edit_form"; // Zwraca widok formularza z komunikatem o błędzie.
        }
    }

    // Metoda obsługująca wyszukiwanie użytkowników po nazwisku.
    @GetMapping("/searchUser")
    public String searchUsersByLastName(@RequestParam String lastName, Model model) {
        // Wyszukuje użytkowników po nazwisku i dodaje do modelu.
        List<UserDTO> users = userService.findUsersByLastName(lastName);
        model.addAttribute("users", users); // Dodaje listę użytkowników do modelu.
        return "admin_user_list"; // Zwraca nazwę widoku listy użytkowników.
    }

    // Metoda obsługująca wyszukiwanie użytkowników po imieniu.
    @PostMapping("/searchUser")
    public String searchUsersByName(@RequestParam String name, Model model) {
        // Wyszukuje użytkowników po imieniu i dodaje do modelu.
        List<UserDTO> users = userService.findUsersByLastName(name);
        model.addAttribute("users", users); // Dodaje listę użytkowników do modelu.
        return "admin_user_list"; // Zwraca nazwę widoku listy użytkowników.
    }
}