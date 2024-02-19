package com.example.projektsklep.controller;


import com.example.projektsklep.service.BasketService;
import com.example.projektsklep.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService; // Serwis zarządzający koszykiem zakupowym.
    private final ProductService productService; // Serwis zarządzający produktami.

    // Konstruktor klasy, wstrzykujący zależności do serwisów basketService i productService.
    public BasketController(BasketService basketService, ProductService productService) {
        this.basketService = basketService;
        this.productService = productService;
    }

    /**
     * Dodaje produkt do koszyka zakupowego.
     *
     * @param productId Identyfikator produktu do dodania.
     * @param quantity Ilość produktu do dodania; domyślnie 1.
     * @param request Obiekt HttpServletRequest, używany do pobrania nagłówka referencyjnego, aby po operacji
     *                przekierować użytkownika z powrotem do poprzedniej strony.
     * @param redirectAttributes Atrybuty przekierowania, używane do przekazywania komunikatów sukcesu lub błędu
     *                           po przekierowaniu.
     * @return Przekierowuje użytkownika z powrotem do strony, z której przyszedł.
     */
    @PostMapping("/add/{productId}")
    public String addToBasket(@PathVariable Long productId,
                              @RequestParam(value = "quantity", defaultValue = "1") int quantity,
                              HttpServletRequest request,
                              RedirectAttributes redirectAttributes) {
        try {
            // Wywołuje metodę serwisu do dodawania produktu do koszyka z określoną ilością.
            basketService.addProductToBasket(productId, quantity);
            // Dodaje komunikat o sukcesie do atrybutów przekierowania.
            redirectAttributes.addFlashAttribute("successMessage", "Produkt dodany do koszyka!");
        } catch (Exception e) {
            // W przypadku wyjątku dodaje komunikat o błędzie do atrybutów przekierowania.
            redirectAttributes.addFlashAttribute("errorMessage", "Nie udało się dodać produktu do koszyka: " + e.getMessage());
        }

        // Pobiera nagłówek referencyjny i przekierowuje użytkownika z powrotem do strony, z której przyszedł.
        String referer = request.getHeader("Referer");
        return "redirect:" + referer;
    }

}


