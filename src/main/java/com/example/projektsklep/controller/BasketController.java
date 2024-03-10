package com.example.projektsklep.controller;


import com.example.projektsklep.service.BasketService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;



    public BasketController(BasketService basketService) {
        this.basketService = basketService;

    }


    @Operation(summary = "Dodaje produkt do koszyka")
    @PostMapping("/add/{productId}")
    public String addToBasket(@PathVariable Long productId,
                              @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        try {
            basketService.addProductToBasket(productId, quantity);
            redirectAttributes.addFlashAttribute("successMessage", "Produkt dodany do koszyka!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Nie udało się dodać produktu do koszyka: " + e.getMessage());
        }

        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}


