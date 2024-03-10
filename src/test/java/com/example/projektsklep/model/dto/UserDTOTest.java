package com.example.projektsklep.model.dto;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOTest {

    @Test
    void userDTOCorrectlyAssignsValues() {
        Long expectedId = 1L;
        String expectedFirstName = "John";
        String expectedLastName = "Doe";
        String expectedEmail = "john.doe@example.com";
        String expectedPassword = "password123";
        AddressDTO expectedAddress = new AddressDTO(1L, "Main Street", "Anytown", "12345", "Country");
        Set<RoleDTO> expectedRoles = new HashSet<>();
        expectedRoles.add(new RoleDTO(1, "USER"));

        UserDTO userDTO = new UserDTO(expectedId, expectedFirstName, expectedLastName, expectedEmail, expectedPassword, expectedAddress, expectedRoles);

        assertAll(
                () -> assertEquals(expectedId, userDTO.id(), "ID should match the provided value"),
                () -> assertEquals(expectedFirstName, userDTO.firstName(), "First name should match the provided value"),
                () -> assertEquals(expectedLastName, userDTO.lastName(), "Last name should match the provided value"),
                () -> assertEquals(expectedEmail, userDTO.email(), "Email should match the provided value"),
                () -> assertEquals(expectedPassword, userDTO.password(), "Password should match the provided value"),
                () -> assertEquals(expectedAddress, userDTO.address(), "Address should match the provided value"),
                () -> assertEquals(expectedRoles, userDTO.roles(), "Roles should match the provided value")
        );
    }
}