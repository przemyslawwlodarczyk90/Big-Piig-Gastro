package com.example.projektsklep.utils;

import com.example.projektsklep.model.dto.AddressDTO;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * Klasa RegistrationForm służy jako formularz rejestracyjny w aplikacji sklepowej,
 * umożliwiając nowym użytkownikom wprowadzenie swoich danych osobowych oraz adresowych
 * podczas tworzenia nowego konta. Zawiera pola niezbędne do zarejestrowania użytkownika,
 * takie jak imię, nazwisko, email, hasło, adres oraz rolę, którą użytkownik ma pełnić w systemie.
 */
@Data
@Builder
public class RegistrationForm {
    private Long id; // Unikalny identyfikator użytkownika
    @NotBlank String firstName; // Imię użytkownika, pole wymagane
    @NotBlank String lastName; // Nazwisko użytkownika, pole wymagane
    @NotBlank String email; // Adres email użytkownika, pole wymagane
    String password; // Hasło użytkownika
    AddressDTO address; // Adres użytkownika jako obiekt DTO
    String role; // Rola użytkownika w systemie
}