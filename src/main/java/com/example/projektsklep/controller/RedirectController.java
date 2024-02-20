package com.example.projektsklep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RedirectController {

    @GetMapping("/") // Mapuje żądanie HTTP GET dla głównego ścieżki aplikacji ("/") na tę metodę.
    public String redirect() {
        // Przekierowuje użytkownika na stronę główną aplikacji ("/home").
        // Używane, gdy użytkownik odwiedza główny URL aplikacji, aby automatycznie przekierować go na konkretną stronę.
        return "redirect:/home";
    }
}