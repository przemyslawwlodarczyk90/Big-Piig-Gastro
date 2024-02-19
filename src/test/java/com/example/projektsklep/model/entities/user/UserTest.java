package com.example.projektsklep.model.entities.user;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.Order;
import com.example.projektsklep.model.entities.role.Role;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {


    @Test
    public void testCreateUserWithName() {
        User user = new User("john.doe@example.com", "hashedPassword", "avatar.png", "John", "Doe");

        assertEquals("john.doe@example.com", user.getEmail());
        assertNull(user.getAddress()); // Address should be initially null
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
    }


    @Test
    public void testUserConstructor_GivenValidData_ShouldCreateUser() {
        // Given
        String email = "johndoe@example.com";
        String passwordHash = "passwordHash";
        String avatarPath = "avatarPath";
        String firstName = "John";
        String lastName = "Doe";

        // When
        User user = new User(email, passwordHash, avatarPath, firstName, lastName);

        // Then
        assertEquals(email, user.getEmail());
        assertEquals(passwordHash, user.getPasswordHash());
        assertEquals(avatarPath, user.getAvatarPath());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
    }

    @Test
    public void testAddOrder_GivenUserAndOrder_ShouldAddOrderToUser() {
        // Given
        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
        Order order = new Order();

        // When
        user.addOrder(order);

        // Then
        assertEquals(1, user.getOrders().size());
        assertTrue(user.getOrders().contains(order));
    }


    @Test
    public void testSetAddress_GivenUserAndAddress_ShouldSetAddress() {
        // Given
        User user = new User("johndoe@example.com", "passwordHash", "avatarPath", "John", "Doe");
        Address address = new Address();

        // When
        user.setAddress(address);

        // Then
        assertEquals(address, user.getAddress());
    }

    @Test
    public void testEquals_WithEqualUsers_ShouldReturnTrue() {
        // Given
        User user1 = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        User user2 = new User("user@example.com", "password", "avatar.png", "John", "Doe");

        // When
        boolean isEqual = user1.equals(user2);

        // Then
        assertTrue(isEqual);
    }

    @Test
    public void testEquals_WithDifferentEmails_ShouldReturnFalse() {
        // Given
        User user1 = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        User user2 = new User("different@example.com", "password", "avatar.png", "John", "Doe");

        // When
        boolean isEqual = user1.equals(user2);

        // Then
        Assertions.assertFalse(isEqual);
    }

    @Test
    public void testHashCode_WithEqualUsers_ShouldReturnEqualHashCodes() {
        // Given
        User user1 = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        User user2 = new User("user@example.com", "password", "avatar.png", "John", "Doe");

        // When
        int hashCode1 = user1.hashCode();
        int hashCode2 = user2.hashCode();

        // Then
        assertEquals(hashCode1, hashCode2);
    }

    @Test
    public void testHashCode_WithDifferentEmails_ShouldReturnDifferentHashCodes() {
        // Given
        User user1 = new User("user@example.com", "password", "avatar.png", "John", "Doe");
        User user2 = new User("different@example.com", "password", "avatar.png", "John", "Doe");

        // When
        int hashCode1 = user1.hashCode();
        int hashCode2 = user2.hashCode();

        // Then
        Assertions.assertNotEquals(hashCode1, hashCode2);
    }

    @Test
    public void testSetFirstName_WithNull_ShouldThrowException() {
        // Given
        User user = new User();


        // When
        // Then
        Assertions.assertThrows(NullPointerException.class, () -> user.setFirstName(null));
    }

    @Test
    public void testSetLastName_WithNull_ShouldThrowException() {
        //Given
        User user = new User();


        // When

        Assertions.assertThrows(NullPointerException.class, () -> user.setFirstName(null));
        // Then
        Assertions.assertThrows(NullPointerException.class, () -> user.setLastName(null));
    }

}
