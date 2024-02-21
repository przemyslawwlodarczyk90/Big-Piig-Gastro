package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.UserService;
import com.example.projektsklep.utils.Basket;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@Controller
@RequestMapping("/account")
public class UserAccountController {


    private final OrderService orderService;
    private final UserService userService;
    private final BasketService basketService;


    public UserAccountController(OrderService orderService, UserService userService, BasketService basketService) {
        this.orderService = orderService;
        this.userService = userService;
        this.basketService = basketService;
    }

    // Wyświetla panel użytkownika z jego danymi.
    @GetMapping("/panel")
    public String showUserPanel(Model model, Authentication authentication) {
        // Pobieranie nazwy użytkownika (email) z danych uwierzytelniających.
        String email = authentication.getName();
        // Pobranie danych użytkownika do wyświetlenia w panelu.
        UserDTO userDTO = userService.getUserDetailsForPanel(email);
        // Dodanie danych użytkownika do modelu.
        model.addAttribute("userDTO", userDTO);
        // Zwrócenie nazwy widoku panelu użytkownika.
        return "userPanel";
    }

    // Lista zamówień użytkownika.
    @GetMapping("/my_orders")
    public String listUserOrders(Model model, Authentication authentication) {
        // Pobranie emaila użytkownika z danych uwierzytelnienia.
        String email = authentication.getName();
        // Pobranie listy zamówień użytkownika.
        List<OrderDTO> orders = orderService.findOrdersForUserEmail(email);
        // Dodanie listy zamówień do modelu.
        model.addAttribute("orders", orders);
        // Zwrócenie nazwy widoku listy zamówień użytkownika.
        return "user_orders";
    }

    // Wyświetlenie formularza edycji danych użytkownika.
    @GetMapping("/edit")
    public String showEditForm(Model model, Authentication authentication) {
        // Pobranie emaila użytkownika z danych uwierzytelnienia.
        String email = authentication.getName();
        // Przygotowanie danych użytkownika do edycji.
        UserDTO userDTO = userService.prepareUserDTOForEdit(email);
        // Dodanie danych użytkownika do modelu.
        model.addAttribute("userDTO", userDTO);
        // Zwrócenie nazwy widoku formularza edycji użytkownika.
        return "user_edit";
    }

    // Aktualizacja danych i adresu użytkownika po wysłaniu formularza.
    @PostMapping("/edit")
    public String updateProfileAndAddress(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                                          BindingResult result,
                                          Model model, Authentication authentication) {
        // Pobranie emaila użytkownika z danych uwierzytelnienia.
        String email = authentication.getName();
        // Aktualizacja danych użytkownika.
        userService.updateUserProfileAndAddress(email, userDTO);
        // Przekierowanie do panelu użytkownika.
        return "redirect:/account/panel";
    }

    // Wyświetlenie koszyka użytkownika.
    @GetMapping("/basket")
    public String viewBasket(Model model) {
        // Pobranie aktualnego stanu koszyka.
        Basket basket = basketService.getCurrentBasket();
        // Dodanie koszyka do modelu.
        model.addAttribute("basket", basket);
        // Zwrócenie nazwy widoku koszyka.
        return "basket_view";
    }

    // Wyświetlenie formularza realizacji zamówienia.
    @GetMapping("/checkoutBasket")
    public String showCheckoutForm(Model model, Authentication authentication) {
        // Pobranie nazwy użytkownika (email) z danych uwierzytelnienia.
        String username = authentication.getName();
        // Pobranie danych użytkownika.
        UserDTO userDTO = userService.findUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        // Przygotowanie zamówienia do realizacji.
        OrderDTO orderDTO = basketService.prepareOrderForCheckout(userDTO.id());
        // Dodanie danych zamówienia do modelu.
        model.addAttribute("order", orderDTO);
        // Zwrócenie nazwy widoku formularza realizacji zamówienia.
        return "order_checkout_form";
    }

    // Przetworzenie realizacji zamówienia po wysłaniu formularza.
    @PostMapping("/checkoutBasket")
    public String processCheckout(@Valid @ModelAttribute("order") OrderDTO orderDTO, BindingResult result, Model model,
                                  HttpServletRequest request, Authentication authentication) {
        // Sprawdzenie, czy formularz zawiera błędy.
        if (result.hasErrors()) {
            // Jeśli tak, powróć do formularza realizacji zamówienia.
            return "order_checkout_form";
        }

        // Pobranie nazwy użytkownika (email) z danych uwierzytelnienia.
        String username = authentication.getName();
        // Przygotowanie i realizacja zamówienia.
        String viewName = basketService.prepareAndPlaceOrder(orderDTO, username, request);
        // Przekierowanie do strony sukcesu zamówienia.
        return "order_success";
    }

    // Aktualizacja ilości produktu w koszyku.
    @PostMapping("/updateProductQuantity/{productId}")
    public String updateProductQuantity(@PathVariable Long productId, @RequestParam("quantity") int quantity) {
        // Aktualizacja ilości produktu w koszyku.
        basketService.updateProductQuantity(productId, quantity);
        // Przekierowanie do koszyka.
        return "redirect:/basket";
    }

    // Strona sukcesu po złożeniu zamówienia.
    @GetMapping("/orderSuccess")
    public String orderSuccess() {
        // Zwrócenie nazwy widoku sukcesu zamówienia.
        return "order_success";
    }
}