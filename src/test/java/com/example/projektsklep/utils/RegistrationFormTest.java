package com.example.projektsklep.utils;

import com.example.projektsklep.model.dto.AddressDTO;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RegistrationFormTest {

    @Test
    void builderShouldCreateFormWithAllFields() {
        AddressDTO addressDTO = AddressDTO.builder()
                .street("Browarna")
                .city("Łuków")
                .postalCode("21-400")
                .country("POLSKA")
                .build();

        RegistrationForm form = RegistrationForm.builder()
                .firstName("Stefek")
                .lastName("Rowicki")
                .email("stefek@example.com")
                .password("password")
                .address(addressDTO)
                .role("USER")
                .build();

        assertNotNull(form);
        assertEquals("Stefek", form.getFirstName());
        assertEquals("Rowicki", form.getLastName());
        assertEquals("stefek@example.com", form.getEmail());
        assertEquals("password", form.getPassword());
        assertEquals(addressDTO, form.getAddress());
        assertEquals("USER", form.getRole());
    }


}