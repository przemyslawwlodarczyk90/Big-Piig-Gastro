package com.example.projektsklep.model.entities.role;

import com.example.projektsklep.model.enums.AdminOrUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


// Klasa testowa RoleTest sprawdza zachowanie klasy Role.
class RoleTest {
    // Test sprawdzający poprawne tworzenie roli na podstawie enuma AdminOrUser.
    @Test
    public void testConstructor_WithAdminOrUser_ShouldCreateRole() {
        // Given: Przygotowanie danych testowych - rola USER z enuma AdminOrUser.
        AdminOrUser adminOrUser = AdminOrUser.USER;

        // When: Tworzenie nowej roli na podstawie adminOrUser.
        Role role = Role.fromAdminOrUser(adminOrUser);

        // Then: Weryfikacja, czy utworzona rola posiada oczekiwaną wartość.
        Assertions.assertEquals(adminOrUser, role.getRoleType());
    }

    // Test sprawdzający, że metoda equals zwraca true dla równych ról.
    @Test
    public void testEquals_WithEqualRoles_ShouldReturnTrue() {
        // Given: Przygotowanie dwóch identycznych ról.
        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.USER);

        // When: Porównanie ról za pomocą metody equals.
        boolean isEqual = role1.equals(role2);

        // Then: Oczekiwane jest, że role są równe.
        Assertions.assertTrue(isEqual);
    }

    // Test sprawdzający, że metoda equals zwraca false dla różnych ról.
    @Test
    public void testEquals_WithDifferentRoles_ShouldReturnFalse() {
        // Given: Przygotowanie dwóch różnych ról.
        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.ADMIN);

        // When: Porównanie ról za pomocą metody equals.
        boolean isEqual = role1.equals(role2);

        // Then: Oczekiwane jest, że role nie są równe.
        Assertions.assertFalse(isEqual);
    }

    // Test sprawdzający, że dwa obiekty Role z tą samą rolą mają taki sam kod hash.
    @Test
    public void testHashCode_WithEqualRoles_ShouldReturnEqualHashCodes() {
        // Given: Przygotowanie dwóch identycznych ról.
        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.USER);

        // When: Obliczenie kodów hash dla obu ról.
        int hashCode1 = role1.hashCode();
        int hashCode2 = role2.hashCode();

        // Then: Oczekiwane jest, że kody hash są takie same.
        Assertions.assertEquals(hashCode1, hashCode2);
    }

    // Test sprawdzający, że dwa obiekty Role z różnymi rolami mają różne kody hash.
    @Test
    public void testHashCode_WithDifferentRoles_ShouldReturnDifferentHashCodes() {
        // Given: Przygotowanie dwóch różnych ról.
        Role role1 = Role.fromAdminOrUser(AdminOrUser.USER);
        Role role2 = Role.fromAdminOrUser(AdminOrUser.ADMIN);

        // When: Obliczenie kodów hash dla obu ról.
        int hashCode1 = role1.hashCode();
        int hashCode2 = role2.hashCode();

        // Then: Oczekiwane jest, że kody hash są różne.
        Assertions.assertNotEquals(hashCode1, hashCode2);
    }

    // Test sprawdzający, że próba utworzenia roli z null jako AdminOrUser powoduje wyjątek.
    @Test
    public void testFromAdminOrUser_WithNull_ShouldThrowException() {
        // Given: Przygotowanie danych testowych - null jako wartość AdminOrUser.
        AdminOrUser adminOrUser = null;

        // When/Then: Oczekiwany jest wyjątek NullPointerException przy próbie utworzenia roli.
        Assertions.assertThrows(NullPointerException.class, () -> Role.fromAdminOrUser(adminOrUser));
    }
}