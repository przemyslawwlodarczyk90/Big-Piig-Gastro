package com.example.projektsklep.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class RedirectController {

    @Operation(summary = "Przekierowanie na stronę główną")
    @GetMapping("/")
    public String redirect() {
        return "redirect:/home";
    }
}