package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.user.User;
import com.example.projektsklep.model.repository.UserRepository;
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
public class UserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        // Przykładowe polskie dane testowe
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
    public void testFindByEmail() {
        String email = "jankowalski@example.com";
        User foundUser = userRepository.findByEmail(email).orElse(null);
        assertNotNull(foundUser, "Użytkownik powinien zostać znaleziony po adresie email.");
        assertEquals("Jan", foundUser.getFirstName(), "Imię znalezionego użytkownika powinno być Jan.");
    }

    @Test
    public void testFindByLastNameIgnoreCaseContaining() {
        List<User> users = userRepository.findByLastNameIgnoreCaseContaining("kow");
        assertFalse(users.isEmpty(), "Powinien zostać znaleziony przynajmniej jeden użytkownik.");
        assertTrue(users.stream().anyMatch(u -> u.getLastName().equalsIgnoreCase("Kowalski")), "Znaleziony użytkownik powinien mieć nazwisko Kowalski.");
    }

    @Test
    public void testAddAndDeleteUser() {
        User newUser = new User();
        newUser.setEmail("krzysztofsikora@example.com");
        newUser.setFirstName("Krzysztof");
        newUser.setLastName("Sikora");
        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser.getId(), "Id zapisanego użytkownika nie powinno być null");

        userRepository.deleteById(savedUser.getId());

        assertFalse(userRepository.findById(savedUser.getId()).isPresent(), "Użytkownik powinien zostać usunięty");
    }


    @Test
    public void testDeleteByIdWithNonExistingId() {
        Long nonExistingId = 999L;
        assertDoesNotThrow(() -> userRepository.deleteById(nonExistingId), "Usunięcie nieistniejącego użytkownika nie powinno spowodować błędu");
    }

    @Test
    public void testSaveUserWithUniqueEmail() {
        User newUser = new User();
        newUser.setEmail("uniqueemail@example.com");
        newUser.setFirstName("Unique");
        newUser.setLastName("Email");
        User savedUser = userRepository.save(newUser);

        assertNotNull(savedUser, "Zapisany użytkownik nie powinien być null");
        assertNotNull(savedUser.getId(), "Id zapisanego użytkownika nie powinno być null");
        assertEquals("uniqueemail@example.com", savedUser.getEmail(), "Email zapisanego użytkownika powinien być uniqueemail@example.com");
    }

    @Test
    public void testUpdateUserDetails() {
        // Załóżmy, że annaNowak ma ID 2
        Long userId = 2L;
        User user = userRepository.findById(userId).orElseThrow(() -> new AssertionError("Użytkownik powinien istnieć"));
        user.setFirstName("AnnaUpdated");
        userRepository.save(user);

        User updatedUser = userRepository.findById(userId).orElseThrow(() -> new AssertionError("Użytkownik powinien istnieć"));
        assertEquals("AnnaUpdated", updatedUser.getFirstName(), "Zaktualizowane imię użytkownika powinno być AnnaUpdated");
    }
}
