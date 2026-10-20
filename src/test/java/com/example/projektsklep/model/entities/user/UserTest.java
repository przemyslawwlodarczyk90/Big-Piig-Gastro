package com.example.projektsklep.model.entities.user;

import com.example.projektsklep.model.entities.adress.Address;
import com.example.projektsklep.model.entities.order.Order;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User userZenek;
    private User userZbyszek;

    @BeforeEach
    void setUp() {
        userZenek = new User("zenek@example.com", "hashedPasswordZenek", "avatarZenek.png", "Zenek", "Zenkowski");
        userZbyszek = new User("zbyszek@example.com", "hashedPasswordZbyszek", "avatarZbyszek.png", "Zbyszek", "Zbyszkowski");
    }

    @AfterEach
    void tearDown() {
        userZenek = null;
        userZbyszek = null;
    }

    @Test
    void testCreateUserWithNameZenek() {
        assertEquals("zenek@example.com", userZenek.getEmail());
        assertNull(userZenek.getAddress());
        assertEquals("Zenek", userZenek.getFirstName());
        assertEquals("Zenkowski", userZenek.getLastName());
    }

    @Test
    void testCreateUserWithNameZbyszek() {
        assertEquals("zbyszek@example.com", userZbyszek.getEmail());
        assertNull(userZbyszek.getAddress());
        assertEquals("Zbyszek", userZbyszek.getFirstName());
        assertEquals("Zbyszkowski", userZbyszek.getLastName());
    }

    @Test
    void testAddOrderToZenek() {
        Order order = new Order();
        userZenek.addOrder(order);

        assertEquals(1, userZenek.getOrders().size());
        assertTrue(userZenek.getOrders().contains(order));
    }

    @Test
    void testSetAddressToZenek() {
        Address address = new Address();
        userZenek.setAddress(address);

        assertEquals(address, userZenek.getAddress());
    }

    @Test
    void testEqualsWithEqualUsers() {
        // Dwie instancje tego samego zapisanego konta sa rowne.
        User anotherZenek = new User("zenek@example.com", "hashedPasswordZenek", "avatarZenek.png", "Zenek", "Zenkowski");
        userZenek.setId(1L);
        anotherZenek.setId(1L);

        assertEquals(userZenek, anotherZenek);
    }

    @Test
    void testEqualsWithDifferentUsers() {
        assertNotEquals(userZenek, userZbyszek);
    }

    @Test
    void testHashCodeWithEqualUsers() {
        User anotherZenek = new User("zenek@example.com", "hashedPasswordZenek", "avatarZenek.png", "Zenek", "Zenkowski");

        assertEquals(userZenek.hashCode(), anotherZenek.hashCode());
    }

    @Test
    void hashCodeRemainsStableAfterPersistenceAndProfileEdit() {
        // Hash nie zmienia sie po nadaniu ID ani zmianie profilu.
        int original = userZenek.hashCode();
        var set = new java.util.HashSet<User>();
        set.add(userZenek);
        userZenek.setId(1L);
        userZenek.setFirstName("Changed");
        userZenek.addOrder(new Order());
        assertEquals(original, userZenek.hashCode());
        assertTrue(set.contains(userZenek));
    }

    @Test
    void testSetFirstNameWithNull() {
        assertThrows(NullPointerException.class, () -> userZenek.setFirstName(null));
    }
}
