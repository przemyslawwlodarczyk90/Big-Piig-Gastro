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

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final OrderService orderService;
    private final ProducerService authorService;
    private final CategoryService categoryService;

    private final ProductService productService;
    private final UserRepository userRepository;


    public AdminController(UserService userService, OrderService orderService, ProducerService authorService, CategoryService categoryService, ProductService productService, UserRepository userRepository) {
        this.userService = userService;
        this.orderService = orderService;
        this.authorService = authorService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.userRepository = userRepository;
    }

    @GetMapping("/panel")
    public String showAdminPanel() {
        return "adminPanel";
    }

    @GetMapping("/user_search")
    public String showUserSearchForm() {
        return "admin_user_search";
    }

    @PostMapping("/user_search")
    public String showUserOrders(@RequestParam String lastName, Model model) {
        List<UserDTO> users = userService.findUsersByLastName(lastName);
        model.addAttribute("users", users);
        return "admin_user_list";
    }


    @GetMapping("/user_details/{userId}")
    public String userDetails(@PathVariable("userId") Long userId, Model model) {
        Optional<UserDTO> userOptional = userService.findUserById(userId);
        if (userOptional.isPresent()) {
            model.addAttribute("user", userOptional.get());
        } else {
            return "redirect:/admin/user_search";
        }
        return "user_details";
    }

    @GetMapping("/author")
    public String showAuthorForm(Model model) {
        model.addAttribute("author", new ProducerDTO(null, ""));
        return "admin_producer_form";
    }

    @PostMapping("/author")
    public String saveAuthor(@Valid @ModelAttribute("author") ProducerDTO authorDTO, BindingResult result, Model model) {
        authorService.saveAuthor(authorDTO);
        return "redirect:/admin/authors";
    }

    @GetMapping("/authors")
    public String listAuthors(Model model,
                              @RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProducerDTO> authorPage = authorService.findAllAuthorsPageable(pageable);
        model.addAttribute("authorPage", authorPage);
        return "admin_producers_list";
    }

    @GetMapping("/addProduct")
    public String showAddProductForm(Model model) {
        productService.prepareAddProductFormModel(model);
        return "admin_add_product";
    }


    @PostMapping("/addProduct")
    public String addProduct(@ModelAttribute ProductDTO productDTO, Model model) {
        productService.saveProductDTO(productDTO);
        productService.prepareAddProductFormModel(model); // Ponownie przygotowuje model
        model.addAttribute("successMessage", "Produkt został dodany.");
        return "admin_add_product";
    }

    @GetMapping("/user_orders/{userId}")
    public String listUserOrders(@PathVariable Long userId, Model model) {
        List<OrderDTO> orders = orderService.findAllOrdersByUserId(userId);
        model.addAttribute("orders", orders);
        return "admin_user_orders";
    }


    @GetMapping("/ordersByStatus")
    public String getOrdersByStatus(@RequestParam(required = false) OrderStatus orderStatus, Model model) {
        orderService.prepareOrdersByStatusModel(orderStatus, model);
        return "orders_by_status";
    }


    @GetMapping("/addCategory")
    public String showAddForm(Model model) {
        categoryService.prepareAddCategoryModel(model);
        return "category_add";
    }

    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute CategoryDTO categoryDTO) {
        try {
            categoryService.addCategoryWithParent(categoryDTO);
            return "redirect:/categories";
        } catch (Exception e) {
            throw new CategoryException("Error adding category", e);
        }
    }

    @GetMapping("/editCategory/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        CategoryDTO categoryDTO = categoryService.getCategoryDTOById(id);
        model.addAttribute("category", categoryDTO);
        return "category_edit";
    }

    @PostMapping("/editCategory/{id}")
    public String editCategory(@PathVariable Long id, @ModelAttribute("category") CategoryDTO categoryDTO) {
        categoryService.updateCategoryDTO(id, categoryDTO);
        return "redirect:/categories";
    }

    @PostMapping("/deleteCategory/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return "redirect:/categories";
    }


    @GetMapping("/editOrderStatus/{orderId}")
    public String showEditOrderForm(@PathVariable Long orderId, Model model) {
        try {
            orderService.prepareEditOrderFormModel(orderId, model);
            return "order_edit_form";
        } catch (OrderNotFoundException e) {
            model.addAttribute("error", "Zamówienie nie znalezione");
            return "error";
        }
    }

    @PostMapping("/editOrderStatus/{orderId}")
    public String updateOrderStatus(@PathVariable Long orderId, @ModelAttribute("order") OrderDTO orderDTO, Model model, HttpServletRequest request) {
        try {
            orderService.updateOrderStatus(orderId, orderDTO.orderStatus());
            String referer = request.getHeader("Referer");
            return "redirect:" + (referer != null ? referer : "/user_orders");
        } catch (OrderNotFoundException e) {
            model.addAttribute("error", "Zamówienie nie znalezione");
            return "error";
        } catch (Exception e) {
            model.addAttribute("error", "Błąd podczas aktualizacji statusu zamówienia");
            return "order_edit_form";
        }
    }


    @GetMapping("/searchUser")

    public String searchUsersByLastName(@RequestParam String lastName, Model model) {
        List<UserDTO> users = userService.findUsersByLastName(lastName);
        model.addAttribute("users", users);
        return "admin_user_list";
    }

    @PostMapping("/searchUser")
    public String searchUsersByName(@RequestParam String name, Model model) {
        List<UserDTO> users = userService.findUsersByLastName(name);
        model.addAttribute("users", users);
        return "admin_user_list";
    }


}
