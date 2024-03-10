package com.example.projektsklep.model.entities.role;

import com.example.projektsklep.model.enums.AdminOrUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class RoleTest {

    @Test
    void testConstructor_WithAdminOrUser_ShouldCreateRole() {

        AdminOrUser adminOrUser = AdminOrUser.USER;

        Role role = Role.fromAdminOrUser(adminOrUser);

        Assertions.assertEquals(adminOrUser, role.getRoleType());
    }


    @Test
    void testEquals_WithEqualRoles_ShouldReturnTrue() {

        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.USER);

        boolean isEqual = role1.equals(role2);

        Assertions.assertTrue(isEqual);
    }

    @Test
    void testEquals_WithDifferentRoles_ShouldReturnFalse() {

        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.ADMIN);

        boolean isEqual = role1.equals(role2);

        Assertions.assertFalse(isEqual);
    }

    @Test
    void testHashCode_WithEqualRoles_ShouldReturnEqualHashCodes() {

        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.USER);

        int hashCode1 = role1.hashCode();
        int hashCode2 = role2.hashCode();

        Assertions.assertEquals(hashCode1, hashCode2);
    }

    @Test
    void testHashCode_WithDifferentRoles_ShouldReturnDifferentHashCodes() {

        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.ADMIN);

        int hashCode1 = role1.hashCode();
        int hashCode2 = role2.hashCode();


        Assertions.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    void testFromAdminOrUser_WithNull_ShouldThrowException() {

        AdminOrUser adminOrUser = null;

        Assertions.assertThrows(NullPointerException.class, () -> Role.fromAdminOrUser(adminOrUser));
    }
}