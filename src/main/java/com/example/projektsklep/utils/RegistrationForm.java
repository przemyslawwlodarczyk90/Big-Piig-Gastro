package com.example.projektsklep.utils;

import com.example.projektsklep.model.dto.AddressDTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Klasa RegistrationForm służy jako formularz rejestracyjny w aplikacji ,
 * umożliwiając nowym użytkownikom wprowadzenie swoich danych osobowych oraz adresowych
 * podczas tworzenia nowego konta. Zawiera pola niezbędne do zarejestrowania użytkownika,
 * takie jak imię, nazwisko, email, hasło, adres oraz rolę, którą użytkownik ma pełnić w systemie.
 */
@Data
@Builder
public class RegistrationForm {
    private Long id;
    @NotBlank String firstName;
    @NotBlank String lastName;
    @NotBlank String email;
    String password;
    AddressDTO address;
    String role;
}