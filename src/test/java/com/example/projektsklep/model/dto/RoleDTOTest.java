package com.example.projektsklep.model.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoleDTOTest {

    @Test
    void recordShouldHoldCorrectValues() {
        Integer expectedId = 1;
        String expectedRoleType = "ADMIN";

        RoleDTO roleDTO = new RoleDTO(expectedId, expectedRoleType);

        assertAll(
                () -> assertEquals(expectedId, roleDTO.id(), "The ID should match the provided value"),
                () -> assertEquals(expectedRoleType, roleDTO.roleType(), "The roleType should match the provided value")
        );
    }

    @Test
    void equalsAndHashCode() {
        Integer id = 1;
        String roleType = "USER";

        RoleDTO roleDTO1 = new RoleDTO(id, roleType);
        RoleDTO roleDTO2 = new RoleDTO(id, roleType);

        assertAll(
                () -> assertEquals(roleDTO1, roleDTO2, "Two RoleDTO objects with the same id and roleType should be equal"),
                () -> assertEquals(roleDTO1.hashCode(), roleDTO2.hashCode(), "hashCode should be equal for two equal RoleDTO objects")
        );
    }
}