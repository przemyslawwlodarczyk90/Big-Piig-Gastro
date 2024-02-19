package com.example.projektsklep.controller;

import com.example.projektsklep.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Collection;

// Klasa kontrolera zarządzająca domową stroną aplikacji.
@Controller
public class HomeController {

    private final UserService userService;

    /**
     * Konstruktor klasy HomeController.
     * @param userService Serwis zarządzający użytkownikami, wstrzyknięty przez Springa.
     */
    public HomeController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Wyświetla stronę główną aplikacji.
     * Metoda określa, czy zalogowany użytkownik posiada role ADMIN lub USER i przekazuje te informacje do modelu.
     *
     * @param model Model przekazywany do widoku, służący do przekazywania danych między kontrolerem a widokiem.
     * @param principal Interfejs Principal zawierający dane zalogowanego użytkownika.
     * @return Nazwa widoku Thymeleaf (plik HTML), który ma zostać wyrenderowany.
     */
    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        // Inicjalizacja zmiennych odpowiedzialnych za przechowywanie informacji o rolach użytkownika.
        boolean isAdmin = false;
        boolean isUser = false;

        // Sprawdzenie, czy zalogowany użytkownik posiada role ADMIN lub USER.
        if (principal != null) {
            // Pobranie kolekcji ról (uprawnień) zalogowanego użytkownika.
            Collection<? extends GrantedAuthority> authorities = ((Authentication) principal).getAuthorities();
            // Sprawdzenie, czy użytkownik posiada konkretną rolę i ustawienie odpowiednich flag.
            isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));
        }

        // Dodanie informacji o rolach do modelu, aby były dostępne w widoku.
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser);

        // Zwrócenie nazwy widoku, który ma zostać wyświetlony.
        return "home";
    }
}