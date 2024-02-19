package com.example.projektsklep.utils;

import com.example.projektsklep.model.dto.AddressDTO;

/**
 * Klasa pomocnicza AddressDTOInitializer służy do inicjalizacji obiektów AddressDTO
 * z domyślnymi, pustymi wartościami. Jest to przydatne w różnych miejscach aplikacji,
 * gdzie wymagane jest dostarczenie nowej instancji AddressDTO, ale nie są dostępne
 * żadne konkretne dane. Ułatwia to zarządzanie domyślnymi wartościami adresu
 * w centralnym miejscu, zwiększając czytelność i ułatwiając utrzymanie kodu.
 */
public class AddressDTOInitializer {

    /**
     * Tworzy nową instancję AddressDTO z domyślnymi wartościami dla wszystkich pól.
     * Domyślne wartości to puste łańcuchy dla ulicy, miasta, kodu pocztowego,
     * kraju oraz null dla identyfikatora, co jest przydatne, gdy potrzebujemy
     * "pustego" obiektu AddressDTO, na przykład do wstępnego wypełnienia formularzy
     * w interfejsie użytkownika lub jako miejsce zastępcze, zanim użytkownik
     * wprowadzi konkretne dane adresowe.
     *
     * @return nową instancję AddressDTO z domyślnymi pustymi wartościami.
     */
    public static AddressDTO createDefault() {
        return new AddressDTO(null, "", "", "", "");
    }
}