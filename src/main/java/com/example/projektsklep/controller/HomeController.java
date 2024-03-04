package com.example.projektsklep.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import java.security.Principal;
import java.util.Collection;


@Controller
public class HomeController {




    @GetMapping("/home")
    public String showHomePage(Model model, Principal principal) {
        boolean isAdmin = false;
        boolean isUser = false;

        if (principal != null) {
            Collection<? extends GrantedAuthority> authorities = ((Authentication) principal).getAuthorities();
            isAdmin = authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
            isUser = authorities.contains(new SimpleGrantedAuthority("ROLE_USER"));
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isUser", isUser);


        return "home";
    }
}