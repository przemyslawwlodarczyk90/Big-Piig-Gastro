package com.example.projektsklep.controller;


import com.example.projektsklep.model.dto.UserDTO;
import com.example.projektsklep.model.enums.AdminOrUser;
import com.example.projektsklep.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


// Kontroler Spring MVC odpowiedzialny za obsługę żądań związanych z listowaniem użytkowników oraz ich rejestracją.
@ControllerAdvice
@Controller
@RequestMapping("/users") // Mapuje żądania dla ścieżki /users do metod w tej klasie.
public class UserListingAndRegistringController {

    // Serwis użytkownika wykorzystywany do operacji na użytkownikach.
    private final UserService userService;

    // Konstruktor do wstrzyknięcia zależności serwisu użytkownika.
    public UserListingAndRegistringController(UserService userService) {
        this.userService = userService;
    }

    // Metoda do wyświetlania listy użytkowników z paginacją.
    @GetMapping
    public String listUsers(Model model, @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size) {
        // Ustawienie paginacji z podanymi parametrami.
        Pageable pageable = PageRequest.of(page, size);
        // Pobranie stronnicowanej listy użytkowników za pomocą serwisu.
        Page<UserDTO> userPage = userService.findAllUsers(pageable);
        // Dodanie listy użytkowników do modelu.
        model.addAttribute("userPage", userPage);
        // Zwrócenie nazwy widoku listy użytkowników.
        return "user_list";
    }

    // Metoda do wyświetlania formularza dodawania nowego użytkownika.
    @GetMapping("/new")
    public String showNewUserForm(Model model) {
        // Inicjalizacja nowego DTO użytkownika.
        UserDTO userDTO = userService.initializeNewUserDTO();
        // Dodanie DTO do modelu, aby można było je wykorzystać w formularzu.
        model.addAttribute("userDTO", userDTO);
        // Zwrócenie nazwy widoku formularza rejestracji.
        return "user_register";
    }

    // Metoda do obsługi żądania POST, rejestrująca nowego użytkownika.
    @PostMapping("/new")
    public String registerUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                               BindingResult result, Model model,
                               @RequestParam("roleType") String roleTypeStr) {
        // Sprawdzenie, czy wystąpiły błędy walidacji formularza.
        if (result.hasErrors()) {
            // Jeśli tak, ponowne wyświetlenie formularza.
            return "user_register";
        }

        // Konwersja typu roli z String na enum.
        AdminOrUser roleType = AdminOrUser.valueOf(roleTypeStr.toUpperCase());

        // Blok try-catch do obsługi potencjalnych wyjątków przy zapisie użytkownika.
        try {
            // Zapis użytkownika z wykorzystaniem serwisu.
            userService.saveUser(userDTO, userDTO.address(), roleType);
        } catch (Exception e) {
            // W przypadku błędu, dodanie informacji o błędzie do modelu i ponowne wyświetlenie formularza.
            model.addAttribute("registrationError", "Nie udało się zarejestrować użytkownika: " + e.getMessage());
            return "user_register";
        }

        // Przekierowanie do strony sukcesu rejestracji po pomyślnym zapisie.
        return "redirect:/users/registrationSuccess";
    }

    // Metoda do wyświetlania strony sukcesu rejestracji.
    @GetMapping("registrationSuccess")
    public String registrationSuccess() {
        // Zwrócenie nazwy widoku sukcesu rejestracji.
        return "registrationSucces";
    }
}