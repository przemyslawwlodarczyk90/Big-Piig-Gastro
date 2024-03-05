package com.example.projektsklep.controller;


import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;




@Controller
@RequestMapping("/users")
public class UserListingAndRegistringController {


    private final UserService userService;


    public UserListingAndRegistringController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Lista użytkowników")
    @GetMapping
    public String listUsers(Model model, @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<UserDTO> userPage = userService.findAllUsers(pageable);
        model.addAttribute("userPage", userPage);
        return "user_list";
    }

    @Operation(summary = "Formularz nowego użytkownika")
    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        UserDTO userDTO = userService.initializeNewUserDTO();
        model.addAttribute("userDTO", userDTO);
        return "user_register";
    }


    @Operation(summary = "Rejestracja użytkownika")
    @PostMapping("/new")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                               BindingResult result, Model model,
                               @RequestParam("roleType") String roleTypeStr) {
        if (result.hasErrors()) {
            return "user_register";
        }

        AdminOrUser roleType = AdminOrUser.valueOf(roleTypeStr.toUpperCase());

        try {
            userService.saveUser(userDTO, userDTO.address(), roleType);
        } catch (Exception e) {
            model.addAttribute("registrationError", "Nie udało się zarejestrować użytkownika: " + e.getMessage());
            return "user_register";
        }

        return "redirect:/users/registrationSuccess";
    }

    @Operation(summary = "Sukces rejestracji")
    @GetMapping("registrationSuccess")
    public String registrationSuccess() {
        return "registrationSucces";
    }
}