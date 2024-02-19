package com.example.projektsklep.utils;

import com.example.projektsklep.model.dto.AddressDTO;

public class AddressDTOInitializer {

    // Metoda do tworzenia nowej instancji AddressDTO z domyślnymi wartościami
    public static AddressDTO createDefault() {
        return new AddressDTO(null, "", "", "", "");
    }
}
