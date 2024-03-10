package com.example.projektsklep.model.repository;

import com.example.projektsklep.model.entities.adress.Address;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE) // Użyj rzeczywistej bazy danych, nie wbudowanej
public class AddressRepositoryIntegrationTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Address testAddress;

    @BeforeEach
    public void setUp() {
        // Utworzenie przykładowego adresu
        testAddress = new Address(null, "Testowa", "Testowe", "00-000", "Testoland");
        entityManager.persist(testAddress);
        entityManager.flush();
    }

    @AfterEach
    public void tearDown() {
        // Opcjonalnie, wyczyść bazę danych po każdym teście
        addressRepository.deleteAll();
    }

    @Test
    public void whenFindById_thenReturnAddress() {
        // Kiedy
        Address found = addressRepository.findById(testAddress.getId()).orElse(null);

        // Wtedy
        assertNotNull(found);
        assertEquals(testAddress.getStreet(), found.getStreet());
    }

    @Test
    public void whenCreateAddress_thenAddressIsInDatabase() {
        // Kiedy
        Address newAddress = new Address(null, "Nowa", "Nowe Miasto", "11-111", "Nowoland");
        addressRepository.save(newAddress);

        // Wtedy
        Address found = entityManager.find(Address.class, newAddress.getId());
        assertNotNull(found);
        assertEquals(newAddress.getStreet(), found.getStreet());
    }

    @Test
    public void whenDeleteAddress_thenAddressIsNotInDatabase() {
        // Kiedy
        addressRepository.deleteById(testAddress.getId());

        // Wtedy
        Address found = entityManager.find(Address.class, testAddress.getId());
        assertNull(found);
    }
}