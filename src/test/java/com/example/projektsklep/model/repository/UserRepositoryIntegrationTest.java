package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
 class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        User janKowalski = new User();
        janKowalski.setEmail("jankowalski@example.com");
        janKowalski.setFirstName("Jan");
        janKowalski.setLastName("Kowalski");
        userRepository.save(janKowalski);

        User annaNowak = new User();
        annaNowak.setEmail("annanowak@example.com");
        annaNowak.setFirstName("Anna");
        annaNowak.setLastName("Nowak");
        userRepository.save(annaNowak);
    }

    @Test
     void testFindByEmail() {
        String email = "jankowalski@example.com";
        User foundUser = userRepository.findByEmail(email).orElse(null);
        assertNotNull(foundUser, "The user should be found by email address.");
        assertEquals("Jan", foundUser.getFirstName(), "The name of the user found should be Jan.");
    }

    @Test
     void testFindByLastNameIgnoreCaseContaining() {
        List<User> users = userRepository.findByLastNameIgnoreCaseContaining("kow");
        assertFalse(users.isEmpty(), "At least one user should be found.");
        assertTrue(users.stream().anyMatch(u -> u.getLastName().equalsIgnoreCase("Kowalski")), "The user found should have the name Kowalski.");
    }

    @Test
     void testAddAndDeleteUser() {
        User newUser = new User();
        newUser.setEmail("krzysztofsikora@example.com");
        newUser.setFirstName("Krzysztof");
        newUser.setLastName("Sikora");
        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser.getId(), "Id of saved user should not be null.");

        userRepository.deleteById(savedUser.getId());

        assertFalse(userRepository.findById(savedUser.getId()).isPresent(), "The user should be removed.");
    }


    @Test
     void testDeleteByIdWithNonExistingId() {
        Long nonExistingId = 999L;
        assertDoesNotThrow(() -> userRepository.deleteById(nonExistingId), "Deleting a non-existent user should not cause an error");
    }
    @Test
     void testSaveUserWithUniqueEmail() {
        User newUser = new User();
        newUser.setEmail("uniqueemail@example.com");
        newUser.setFirstName("Unique");
        newUser.setLastName("Email");
        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser, "The saved user should not be null.");
        assertNotNull(savedUser.getId(), "Id of saved user should not be null.");
        assertEquals("uniqueemail@example.com", savedUser.getEmail(), "Email of enrolled user should be uniqueemail@example.com");
    }

    @Test
     void testUpdateUserDetails() {

        Long userId = 2L;
        User user = userRepository.findById(userId).orElseThrow(() -> new AssertionError("The user should exist."));
        user.setFirstName("AnnaUpdated");
        userRepository.save(user);

        User updatedUser = userRepository.findById(userId).orElseThrow(() -> new AssertionError("The user should exist"));
        assertEquals("AnnaUpdated", updatedUser.getFirstName(), "The updated user name should be AnnaUpdated");
    }
}