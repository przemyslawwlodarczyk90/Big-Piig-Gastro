package com.example.projektsklep.controller;


import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.model.dto.AddressDTO;

import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.Map;


@Controller
@RequestMapping("/orders")
public class OrderController {


    private final OrderService orderService;
    private final BasketService basketService;
    private final UserService userService;


    public OrderController(OrderService orderService, BasketService basketService, UserService userService) {
        this.orderService = orderService;
        this.basketService = basketService;
        this.userService = userService;
    }


    @Operation(summary = "Wyświetla listę zamówień")
    @GetMapping
    public String listOrders(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Map<String, Object> response = orderService.findAllOrdersWithPagination(pageable); // Pobieranie zamówień z paginacją.

        model.addAllAttributes(response);

        return "order_list";
    }


    @Operation(summary = "Szczegóły zamówienia")
    @ExceptionHandler(OrderNotFoundException.class)
    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model) {

        orderService.findOrderDTOById(orderId)
                .ifPresentOrElse(
                        orderDTO -> model.addAttribute("order", orderDTO),
                        () -> model.addAttribute("error", "Zamówienie nie znalezione")
                );
        return orderService.findOrderDTOById(orderId).isPresent() ? "order_details" : "error";
    }


    @Operation(summary = "Tworzenie zamówienia z koszyka")
    @PostMapping("/create")
    public String createOrderFromBasket(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserDTO userDTO = userService.findUserByEmail(username).orElseThrow(() -> new RuntimeException("User not found"));

        boolean useDifferentAddress = "on".equals(request.getParameter("differentAddress"));
        AddressDTO newShippingAddress = useDifferentAddress ? new AddressDTO(
                null,
                request.getParameter("street"),
                request.getParameter("city"),
                request.getParameter("postalCode"),
                request.getParameter("country")) : null;

        String redirectUrl = basketService.finalizeOrderAndRedirect(userDTO.id(), newShippingAddress, useDifferentAddress);

        redirectAttributes.addFlashAttribute("success", "Zamówienie zostało złożone.");
        return redirectUrl;
    }

}