package com.example.projektsklep.model.entities.role;

import com.example.projektsklep.model.enums.AdminOrUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class RoleTest {
    @Test
    public void testConstructor_WithAdminOrUser_ShouldCreateRole() {
        // Given
        AdminOrUser adminOrUser = AdminOrUser.USER;

        // When
        Role role = Role.fromAdminOrUser(adminOrUser);

        // Then
        Assertions.assertEquals(adminOrUser, role.getRoleType());
    }

    @Test
    public void testEquals_WithEqualRoles_ShouldReturnTrue() {
        // Given
        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.USER);

        // When
        boolean isEqual = role1.equals(role2);

        // Then
        Assertions.assertTrue(isEqual);
    }

    @Test
    public void testEquals_WithDifferentRoles_ShouldReturnFalse() {
        // Given
        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.ADMIN);

        // When
        boolean isEqual = role1.equals(role2);

        // Then
        Assertions.assertFalse(isEqual);
    }

    @Test
    public void testHashCode_WithEqualRoles_ShouldReturnEqualHashCodes() {
        // Given
        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.USER);

        // When
        int hashCode1 = role1.hashCode();
        int hashCode2 = role2.hashCode();

        // Then
        Assertions.assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_WithDifferentRoles_ShouldReturnDifferentHashCodes() {
        // Given
        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.ADMIN);

        // When
        int hashCode1 = role1.hashCode();
        int hashCode2 = role2.hashCode();

        // Then
        Assertions.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testFromAdminOrUser_WithNull_ShouldThrowException() {
        // Given
        AdminOrUser adminOrUser = null;

        // When
        // Then
        Assertions.assertThrows(NullPointerException.class, () -> Role.fromAdminOrUser(adminOrUser));
    }


}