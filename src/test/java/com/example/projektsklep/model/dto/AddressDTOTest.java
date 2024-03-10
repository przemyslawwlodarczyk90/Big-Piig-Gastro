package com.example.projektsklep.model.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class AddressDTOTest {

    @Test
    public void testConstructor() {
        Long id = 1L;
        String street = "Główna 12";
        String city = "Warszawa";
        String postalCode = "00-001";
        String country = "Polska";

        AddressDTO addressDTO = new AddressDTO(id, street, city, postalCode, country);

        Assertions.assertEquals(id, addressDTO.id());
        Assertions.assertEquals(street, addressDTO.street());
        Assertions.assertEquals(city, addressDTO.city());
        Assertions.assertEquals(postalCode, addressDTO.postalCode());
        Assertions.assertEquals(country, addressDTO.country());
    }



}