package com.example.projektsklep.model.entities.user;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.role.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    // Testuje tworzenie użytkownika z podstawowymi informacjami.
    @Test
    public void testCreateUserWithName() {
        // Inicjalizacja użytkownika z danymi.
        User user = new User("john.doe@example.com", "hashedPassword", "avatar.png", "John", "Doe");

        // Sprawdzenie, czy podane dane zostały poprawnie przypisane.
        assertEquals("john.doe@example.com", user.getEmail());
        assertNull(user.getAddress()); // Adres powinien być początkowo null.
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
    }

    // Testuje konstruktor klasy User przy użyciu poprawnych danych.
    @Test
    public void testUserConstructor_GivenValidData_ShouldCreateUser() {
        // Given: Dane do stworzenia użytkownika.
        String email = "johndoe@example.com";
        String passwordHash = "passwordHash";
        String avatarPath = "avatarPath";
        String firstName = "John";
        String lastName = "Doe";

        // When: Tworzenie nowego użytkownika.
        User user = new User(email, passwordHash, avatarPath, firstName, lastName);

        // Then: Sprawdzenie czy użytkownik został poprawnie utworzony.
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(avatarPath, user.getAvatarPath());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
    }

    // Testuje dodanie zamówienia do użytkownika.
    @Test
    public void testAddOrder_GivenUserAndOrder_ShouldAddOrderToUser() {
        // Given: Użytkownik i zamówienie.
        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
        Order order = new Order();

        // When: Dodanie zamówienia do użytkownika.
        user.addOrder(order);

        // Then: Sprawdzenie, czy zamówienie zostało dodane.
        assertEquals(1, user.getOrders().size());
        assertTrue(user.getOrders().contains(order));
    }

    // Testuje ustawienie adresu dla użytkownika.
    @Test
    public void testSetAddress_GivenUserAndAddress_ShouldSetAddress() {
        // Given: Użytkownik i adres.
        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
        Address address = new Address();

        // When: Ustawienie adresu użytkownika.
        user.setAddress(address);

        // Then: Sprawdzenie, czy adres został ustawiony.
        assertEquals(address, user.getAddress());
    }

    // Testuje równość dwóch użytkowników.
    @Test
    public void testEquals_WithEqualUsers_ShouldReturnTrue() {
        // Given: Dwa identyczne obiekty użytkownika.
        User user1 = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        User user2 = new User("user@example.com", "password", "avatar.png", "John", "Doe");

        // When: Porównanie użytkowników.
        boolean isEqual = user1.equals(user2);

        // Then: Użytkownicy są równi.
        assertTrue(isEqual);
    }

    // Testuje różnicę między użytkownikami z różnymi adresami email.
    @Test
    public void testEquals_WithDifferentEmails_ShouldReturnFalse() {
        // Given: Dwa różne obiekty użytkownika (różne adresy email).
        User user1 = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        User user2 = new User("different@example.com", "password", "avatar.png", "John", "Doe");

        // When: Porównanie użytkowników.
        boolean isEqual = user1.equals(user2);

        // Then: Użytkownicy nie są równi.
        Assertions.assertFalse(isEqual);
    }

    // Testuje, czy identyczni użytkownicy mają taki sam kod hash.
    @Test
    public void testHashCode_WithEqualUsers_ShouldReturnEqualHashCodes() {
        // Given: Dwa identyczne obiekty użytkownika.
        User user1 = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        User user2 = new User("user@example.com", "password", "avatar.png", "John", "Doe");

        // When: Obliczenie kodów hash.
        int hashCode1 = user1.hashCode();
        int hashCode2 = user2.hashCode();

        // Then: Kody hash są równe.
        assertEquals(hashCode1, hashCode2);
    }

    // Testuje, czy różni użytkownicy mają różne kody hash.
    @Test
    public void testHashCode_WithDifferentEmails_ShouldReturnDifferentHashCodes() {
        // Given: Dwa różne obiekty użytkownika.
        User user1 = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        User user2 = new User("different@example.com", "password", "avatar.png", "John", "Doe");

        // When: Obliczenie kodów hash.
        int hashCode1 = user1.hashCode();
        int hashCode2 = user2.hashCode();

        // Then: Kody hash są różne.
        Assertions.assertNotEquals(hashCode1, hashCode2);
    }

    // Testuje, czy ustawienie imienia na null powoduje wyjątek.
    @Test
    public void testSetFirstName_WithNull_ShouldThrowException() {
        // Given: Nowy obiekt użytkownika.
        User user = new User();

        // When/Then: Oczekiwany jest wyjątek NullPointerException przy próbie ustawienia imienia na null.
        Assertions.assertThrows(NullPointerException.class, () -> user.setFirstName(null));
    }

    // Testuje, czy ustawienie nazwiska na null powoduje wyjątek.
    @Test
    public void testSetLastName_WithNull_ShouldThrowException() {
        // Given: Nowy obiekt użytkownika.
        User user = new User();

        // When/Then: Oczekiwany jest wyjątek NullPointerException przy próbie ustawienia nazwiska na null.
        Assertions.assertThrows(NullPointerException.class, () -> user.setLastName(null));
    }
}