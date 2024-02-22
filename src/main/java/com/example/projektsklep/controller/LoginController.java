package com.example.projektsklep.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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
        return "login";
    }
}