package com.example.projektsklep.controller;


import com.example.projektsklep.exception.CategoryException;
import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.model.dto.*;
import com.example.projektsklep.model.enums.OrderStatus;
import com.example.projektsklep.service.*;
import io.swagger.v3.oas.annotations.Operation;
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


@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final OrderService orderService;
    private final ProducerService authorService;
    private final CategoryService categoryService;
    private final ProductService productService;


    public AdminController(UserService userService, OrderService orderService, ProducerService authorService, CategoryService categoryService, ProductService productService) {
        this.userService = userService;
        this.orderService = orderService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.productService = productService;
    }


    @Operation(summary = "Wyświetla panel administratora")
    @GetMapping("/panel")
    public String showAdminPanel() {
        return "adminPanel";
    }

    @Operation(summary = "Formularz wyszukiwania użytkownika")
    @GetMapping("/user_search")
    public String showUserSearchForm() {
        return "admin_user_search";
    }

    @Operation(summary = "Wyświetla listę zamówień użytkownika na podstawie nazwiska")
    @PostMapping("/user_search")
    public String showUserOrders(@RequestParam String lastName, Model model) {
        List<UserDTO> users = userService.findUsersByLastName(lastName);
        model.addAttribute("users", users);
        return "admin_user_list";
    }

    @Operation(summary = "Szczegóły użytkownika")
    @GetMapping("/user_details/{userId}")
    public String userDetails(@PathVariable("userId") Long userId, Model model) {
        Optional<UserDTO> userOptional = userService.findUserById(userId);
        if(userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
            return "user_details";
        } else {
            return "redirect:/admin/user_search";
        }
    }
    @Operation(summary = "Wyświetla formularz dodawania autora")
    @GetMapping("/author")
    public String showAuthorForm(Model model) {
        model.addAttribute("author", new ProducerDTO(null, ""));
        return "admin_producer_form";
    }

    @Operation(summary = "Zapisuje autora i przekierowuje do listy autorów")
    @PostMapping("/author")
    public String saveAuthor(@Valid @ModelAttribute("author") ProducerDTO authorDTO, BindingResult result) {
        if (!result.hasErrors()) {
            authorService.saveAuthor(authorDTO);
            return "redirect:/admin/authors";
        } else {
            return "admin_producer_form";
        }
    }

    @Operation(summary = "Wyświetla listę autorów")
    @GetMapping("/authors")
    public String listAuthors(Model model, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProducerDTO> authorPage = authorService.findAllAuthorsPageable(pageable);
        model.addAttribute("authorPage", authorPage);
        return "admin_producers_list";
    }

    @Operation(summary = "Wyświetla formularz dodania produktu")
    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        productService.prepareAddProductFormModel(model);
        return "admin_add_product";
    }

    @Operation(summary = "Wyświetla formularz dodania produktu")
    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute ProductDTO productDTO, Model model) {
        productService.saveProductDTO(productDTO);
        productService.prepareAddProductFormModel(model);
        model.addAttribute("successMessage", "Produkt został dodany.");
        return "admin_add_product";
    }

    @Operation(summary = "Wyświetla zamówienia użytkownika")
    @GetMapping("/user_orders/{userId}")
    public String listUserOrders(@PathVariable Long userId, Model model) {
        List<OrderDTO> orders = orderService.findAllOrdersByUserId(userId);
        model.addAttribute("orders", orders);
        return "admin_user_orders";
    }


    @Operation(summary = "Wyświetla zamówienia według statusu")
    @GetMapping("/ordersByStatus")
    public String getOrdersByStatus(@RequestParam(required = false) OrderStatus orderStatus, Model model) {
        orderService.prepareOrdersByStatusModel(orderStatus, model);
        return "orders_by_status";
    }


    @Operation(summary = "Wyświetla formularz dodawania kategorii")
    @GetMapping("/addCategory")
    public String showAddForm(Model model) {
        categoryService.prepareAddCategoryModel(model);
        return "category_add";
    }

    @Operation(summary = "Dodaje kategorię i przekierowuje do listy kategorii")
    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute CategoryDTO categoryDTO) {
        try {
            categoryService.addCategoryWithParent(categoryDTO);
            return "redirect:/categories";
        } catch (Exception e) {
            throw new CategoryException("Error adding category", e);
        }
    }

    @Operation(summary = "Wyświetla formularz edycji kategorii")
    @GetMapping("/editCategory/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.getCategoryDTOById(id);
        model.addAttribute("category", categoryDTO);
        return "category_edit";
    }

    @Operation(summary = "Aktualizuje kategorię i przekierowuje do listy kategorii")
    @PostMapping("/editCategory/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute("category") CategoryDTO categoryDTO) {
        categoryService.updateCategoryDTO(id, categoryDTO);
        return "redirect:/categories";
    }

    @Operation(summary = "Usuwa kategorię i przekierowuje do listy kategorii")
    @PostMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }


    @Operation(summary = "Wyświetla formularz edycji statusu zamówienia")
    @GetMapping("/editOrderStatus/{orderId}")
    public String showEditOrderForm(@PathVariable Long orderId, Model model) {
        try {
            orderService.prepareEditOrderFormModel(orderId, model);
            return "order_edit_form";
        } catch (OrderNotFoundException e) {
            model.addAttribute("error", "Order not found");
            return "error";
        }
    }

    @Operation(summary = "Aktualizuje status zamówienia i przekierowuje do listy zamówień według statusu")
    @PostMapping("/editOrderStatus/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @ModelAttribute("order") OrderDTO orderDTO) {
        orderService.updateOrderStatus(orderId, orderDTO.orderStatus());
        return "redirect:/admin/ordersByStatus";
    }


    @Operation(summary = "Wyszukuje użytkowników po nazwisku-formularz")
    @GetMapping("/searchUser")
    public String searchUsersByLastNameForm(@RequestParam String lastName, Model model) {
        List<UserDTO> users = userService.findUsersByLastName(lastName);
        model.addAttribute("users", users);
        return "admin_user_list";
    }


    @Operation(summary = "Wyszukuje użytkowników po nazwisku-formularz")
    @PostMapping("/searchUser")
    public String searchUsersByLastName(@RequestParam String name, Model model) {

        List<UserDTO> users = userService.findUsersByLastName(name);
        model.addAttribute("users", users);
        return "admin_user_list";
    }
}