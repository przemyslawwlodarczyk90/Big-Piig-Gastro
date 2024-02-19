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

@ControllerAdvice
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

    @GetMapping("/panel")
    public String showUserPanel(Model model, Authentication authentication) {
        String email = authentication.getName();
        UserDTO userDTO = userService.getUserDetailsForPanel(email);
        model.addAttribute("userDTO", userDTO);
        return "userPanel";
    }

    @GetMapping("/my_orders")
    public String listUserOrders(Model model, Authentication authentication) {
        String email = authentication.getName();
        List<OrderDTO> orders = orderService.findOrdersForUserEmail(email);
        model.addAttribute("orders", orders);
        return "user_orders"; // Strona z zamówieniami użytkownika
    }

    @GetMapping("/edit")
    public String showEditForm(Model model, Authentication authentication) {
        String email = authentication.getName();
        UserDTO userDTO = userService.prepareUserDTOForEdit(email);
        model.addAttribute("userDTO", userDTO);
        return "user_edit";
    }


    @PostMapping("/edit")
    public String updateProfileAndAddress(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                                          BindingResult result,
                                          Model model, Authentication authentication) {


        String email = authentication.getName();
        userService.updateUserProfileAndAddress(email, userDTO);
        return "redirect:/account/panel";
    }


    @GetMapping("/basket")
    public String viewBasket(Model model) {
        Basket basket = basketService.getCurrentBasket();
        model.addAttribute("basket", basket);
        return "basket_view";
    }


    @GetMapping("/checkoutBasket")
    public String showCheckoutForm(Model model, Authentication authentication) {
        String username = authentication.getName();
        UserDTO userDTO = userService.findUserByEmail(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        OrderDTO orderDTO = basketService.prepareOrderForCheckout(userDTO.id());
        model.addAttribute("order", orderDTO);
        return "order_checkout_form";
    }

    @PostMapping("/checkoutBasket")
    public String processCheckout(@Valid @ModelAttribute("order") OrderDTO orderDTO, BindingResult result, Model model,
                                  HttpServletRequest request, Authentication authentication) {
        if (result.hasErrors()) {
            return "order_checkout_form";
        }

        String username = authentication.getName();
        String viewName = basketService.prepareAndPlaceOrder(orderDTO, username, request);
        return "order_success";
    }


    @PostMapping("/updateProductQuantity/{productId}")
    public String updateProductQuantity(@PathVariable Long productId, @RequestParam("quantity") int quantity) {
        basketService.updateProductQuantity(productId, quantity);
        return "redirect:/basket";
    }


    @GetMapping("/orderSuccess")
    public String orderSuccess() {
        return "order_success"; //
    }


}