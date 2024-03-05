package com.example.projektsklep.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/user")
public class LoginController {


    @Operation(summary = "Wyświetla formularz logowania")
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}