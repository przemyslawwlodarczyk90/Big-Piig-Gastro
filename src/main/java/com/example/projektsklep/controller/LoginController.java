package com.example.projektsklep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Kontroler obsługujący logowanie użytkowników.
 * Odpowiada za wyświetlenie strony logowania.
 */
@Controller
@RequestMapping("/user")
public class LoginController {

    /**
     * Obsługuje żądanie GET do endpointu "/login", służące do wyświetlenia strony logowania.
     *
     * @return Nazwa widoku Thymeleaf (plik HTML), który zawiera formularz logowania.
     */
    @GetMapping("/login")
    public String login() {
        // Zwraca nazwę widoku strony logowania, który zostanie wyświetlony użytkownikowi.
        return "login";
    }
}