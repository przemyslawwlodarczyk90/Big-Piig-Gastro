package com.example.projektsklep.controller;

import com.example.projektsklep.model.dto.OrderDTO;
import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.OrderService;
import com.example.projektsklep.service.UserService;
import com.example.projektsklep.utils.Basket;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(summary = "Pokazuje panel użytkownika")
    @GetMapping("/panel")
    public String showUserPanel(Model model, Authentication authentication) {
        String email = authentication.getName();
        UserDTO userDTO = userService.getUserDetailsForPanel(email);
        model.addAttribute("userDTO", userDTO);
        return "userPanel";
    }

    @Operation(summary = "Wyświetla zamówienia użytkownika")
    @GetMapping("/my_orders")
    public String listUserOrders(Model model, Authentication authentication) {
        String email = authentication.getName();
        List<OrderDTO> orders = orderService.findOrdersForUserEmail(email);
        model.addAttribute("orders", orders);
        return "user_orders";
    }


    @Operation(summary = "Pokazuje formularz edycji użytkownika")
    @GetMapping("/edit")
    public String showEditForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        UserDTO userDTO = userService.prepareUserDTOForEdit(email);
        model.addAttribute("userDTO", userDTO);
        return "user_edit";
    }


    @Operation(summary = "Aktualizuje profil i adres użytkownika")
    @PostMapping("/edit")
    public String updateProfileAndAddress(@Valid @ModelAttribute("userDTO") UserDTO userDTO,

                                          Authentication authentication) {

        String email = authentication.getName();
        userService.updateUserProfileAndAddress(email, userDTO);
        return "redirect:/account/panel";
    }

    @Operation(summary = "Pokazuje koszyk użytkownika")
    @GetMapping("/basket")
    public String viewBasket(Model model) {

        Basket basket = basketService.getCurrentBasket();
        model.addAttribute("basket", basket);
        return "basket_view";
    }


    @Operation(summary = "Pokazuje formularz realizacji zamówienia")
    @GetMapping("/checkoutBasket")
    public String showCheckoutForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        UserDTO userDTO = userService.findUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        OrderDTO orderDTO = basketService.prepareOrderForCheckout(userDTO.id());
        model.addAttribute("order", orderDTO);
        return "order_checkout_form";
    }


    @Operation(summary = "Przetwarza realizację zamówienia")
    @PostMapping("/checkoutBasket")
    public String processCheckout(@Valid @ModelAttribute("order") OrderDTO orderDTO, BindingResult result
    ) {
        if (result.hasErrors()) {
            return "order_checkout_form";
        }

        return "order_success";
    }


    @Operation(summary = "Aktualizuje ilość produktu w koszyku")
    @PostMapping("/updateProductQuantity/{productId}")
    public String updateProductQuantity(@PathVariable Long productId, @RequestParam("quantity") int quantity) {
        basketService.updateProductQuantity(productId, quantity);
        return "redirect:/basket";
    }

    @Operation(summary = "Pokazuje stronę sukcesu zamówienia")
    @GetMapping("/orderSuccess")
    public String orderSuccess() {
        return "order_success";
    }
}