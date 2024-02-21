package com.example.projektsklep.controller;


import com.example.projektsklep.exception.OrderNotFoundException;
import com.example.projektsklep.model.dto.AddressDTO;

import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.UserService;
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

    // Wyświetla listę zamówień z paginacją.
    @GetMapping
    public String listOrders(Model model,
                             @RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size); // Tworzenie obiektu Pageable do paginacji.
        Map<String, Object> response = orderService.findAllOrdersWithPagination(pageable); // Pobieranie zamówień z paginacją.

        model.addAllAttributes(response); // Dodanie odpowiedzi do modelu.

        return "order_list"; // Zwraca nazwę widoku z listą zamówień.
    }

    // Szczegóły zamówienia; obsługuje wyjątek, gdy zamówienie nie zostanie znalezione.
    @ExceptionHandler(OrderNotFoundException.class) // Anotacja wskazuje, że ta metoda obsługuje wyjątki typu OrderNotFoundException.
    @GetMapping("/{orderId}")
    public String orderDetails(@PathVariable Long orderId, Model model) {
        // Wywołanie metody serwisu, aby pobrać szczegóły zamówienia. Jeśli zamówienie istnieje, jest ono dodawane do modelu.
        // W przeciwnym razie do modelu dodawany jest komunikat o błędzie.
        orderService.findOrderDTOById(orderId)
                .ifPresentOrElse(
                        orderDTO -> model.addAttribute("order", orderDTO),
                        () -> model.addAttribute("error", "Zamówienie nie znalezione")
                );
        // Zwraca widok 'order_details' jeśli zamówienie zostało znalezione, w przeciwnym razie zwraca widok błędu.
        return orderService.findOrderDTOById(orderId).isPresent() ? "order_details" : "error";
    }

    // Tworzy zamówienie na podstawie zawartości koszyka.
    @PostMapping("/create")
    public String createOrderFromBasket(RedirectAttributes redirectAttributes, HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Pobranie aktualnego uwierzytelnienia.
        String username = authentication.getName(); // Pobranie nazwy użytkownika z uwierzytelnienia.

        UserDTO userDTO = userService.findUserByEmail(username).orElseThrow(() -> new RuntimeException("User not found")); // Pobranie DTO użytkownika.

        boolean useDifferentAddress = "on".equals(request.getParameter("differentAddress")); // Sprawdzenie, czy użyto innego adresu.
        AddressDTO newShippingAddress = useDifferentAddress ? new AddressDTO(
                null,
                request.getParameter("street"),
                request.getParameter("city"),
                request.getParameter("postalCode"),
                request.getParameter("country")) : null; // Utworzenie nowego adresu wysyłki, jeśli wymagane.

        String redirectUrl = basketService.finalizeOrderAndRedirect(userDTO.id(), newShippingAddress, useDifferentAddress); // Finalizacja zamówienia i przekierowanie.

        redirectAttributes.addFlashAttribute("success", "Zamówienie zostało złożone."); // Dodanie wiadomości sukcesu.
        return redirectUrl;
    }

}